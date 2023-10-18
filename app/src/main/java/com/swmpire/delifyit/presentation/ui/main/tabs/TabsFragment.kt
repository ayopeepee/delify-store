package com.swmpire.delifyit.presentation.ui.main.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import com.swmpire.delifyit.R
import com.swmpire.delifyit.databinding.FragmentTabsBinding


class TabsFragment : Fragment() {

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
    }

}