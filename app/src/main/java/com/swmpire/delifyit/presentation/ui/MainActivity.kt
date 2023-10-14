package com.swmpire.delifyit.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.swmpire.delifyit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_menu)
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
                    as NavHostFragment)

        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        // hide bottom menu when needed
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.signUpFragment) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}