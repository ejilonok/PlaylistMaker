package com.ejilonok.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.ActivityMainBinding
import com.ejilonok.playlistmaker.main.presentation.MainViewModel
import com.ejilonok.playlistmaker.main.ui.common.gone
import com.ejilonok.playlistmaker.main.ui.common.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.onCreate()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.gone()
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.show()
    }
}
