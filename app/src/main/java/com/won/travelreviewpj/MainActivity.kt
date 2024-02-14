package com.won.travelreviewpj

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.won.travelreviewpj.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        bottomNavigation()
    }

    private fun bottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.bottom_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination == navController.findDestination(R.id.fragment_travel) ||
               destination == navController.findDestination(R.id.fragment_record) ||
                destination == navController.findDestination(R.id.fragment_schedule) ||
                destination == navController.findDestination(R.id.fragment_map)) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else binding.bottomNavigationView.visibility = View.GONE
        }
    }
}