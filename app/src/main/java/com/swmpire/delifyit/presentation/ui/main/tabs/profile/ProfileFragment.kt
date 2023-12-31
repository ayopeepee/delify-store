package com.swmpire.delifyit.presentation.ui.main.tabs.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentProfileBinding
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.uitl.TimeIntervals
import com.swmpire.delifyit.utils.findTopNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(toolbar.menu){
                findItem(R.id.sign_out).setOnMenuItemClickListener {
                    onSignOutButtonPressed()
                    true
                }
                findItem(R.id.edit).setOnMenuItemClickListener {
                    findNavController()
                        .navigate(ProfileFragmentDirections.actionProfileFragmentToUpdateStoreFragment())
                    true
                }
            }

            swipeRefresh.setOnRefreshListener {
                profileViewModel.getStore()
            }

            tabLayoutDateRanges.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position) {
                        0 -> {
                            profileViewModel.getTotalOrdersByTimeInterval(TimeIntervals.DAY)
                            profileViewModel.getTotalRevenueByTimeInterval(TimeIntervals.DAY)
                        }
                        1 -> {
                            profileViewModel.getTotalOrdersByTimeInterval(TimeIntervals.MONTH)
                            profileViewModel.getTotalRevenueByTimeInterval(TimeIntervals.MONTH)
                        }
                        2 -> {
                            profileViewModel.getTotalOrdersByTimeInterval(TimeIntervals.YEAR)
                            profileViewModel.getTotalRevenueByTimeInterval(TimeIntervals.YEAR)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })
        }
        profileViewModel.getCurrentOrdersAndRevenue()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    profileViewModel.getStoreFlow.collect { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.VISIBLE
                                }
                            }

                            is NetworkResult.Success -> {
                                val store = result.data
                                if (store != null) {
                                    with(binding) {
                                        progressHorizontal.visibility = View.GONE
                                        swipeRefresh.isRefreshing = false

                                        toolbar.title = store.name

                                        Glide.with(requireContext())
                                            .load(store.profilePictureUrl)
                                            .placeholder(R.drawable.placeholder_image)
                                            .into(circleImageViewProfile)
                                        textViewName.text = store.name
                                        textViewDescription.text = store.description
                                    }

                                }
                            }

                            is NetworkResult.Error -> {
                                with(binding) {
                                    progressHorizontal.visibility = View.GONE
                                    swipeRefresh.isRefreshing = false
                                }
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is NetworkResult.Idle -> {}
                        }
                    }
                }
                launch {
                    profileViewModel.getTotalOrdersFlow.collect { result ->
                        with(binding) {
                            textViewTotalOrders.text = result.toString()
                        }
                    }
                }
                launch {
                    profileViewModel.getTotalRevenueFlow.collect { result ->
                        with(binding) {
                            textViewTotalRevenue.text = resources.getString(R.string.price_tenge, result)
                        }
                    }
                }
            }
        }
    }

    private fun onSignOutButtonPressed() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.approvement))
            .setMessage(resources.getString(R.string.sign_out_confirm))
            .setNegativeButton(resources.getString(R.string.no)) { _, _ ->}
            .setPositiveButton(resources.getString(R.string.yes)) { _, _, ->
                profileViewModel.signOut()
                findTopNavController().navigate(R.id.signUpFragment, null, navOptions {
                    popUpTo(R.id.tabsFragment) {
                        inclusive = true
                    }
                })
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}