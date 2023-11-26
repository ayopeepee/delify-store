package com.swmpire.delifyit.presentation.ui.main.tabs

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentTabsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TabsFragment : Fragment() {

    private val tabsViewModel: TabsViewModel by viewModels()
    private var _binding: FragmentTabsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost = childFragmentManager.findFragmentById(R.id.nav_host_tabs_container) as NavHost
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomMenu, navController)

        // DON'T TOUCH IT (removes bottom menu when keyboard is opened)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                binding.bottomMenu.visibility = View.GONE
            } else {
                binding.bottomMenu.visibility = View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tabsViewModel.numberOfOrdersToProceedFlow.collect { result ->
                    with(binding.bottomMenu.getOrCreateBadge(R.id.orders)) {
                        if (result > 0) {
                            this.isVisible = true
                            this.number = result
                        }
                        else {
                            this.isVisible = false
                        }
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}