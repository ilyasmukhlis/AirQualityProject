package com.example.airquality

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.airquality.databinding.ActivityMainBinding
import com.example.airquality.utils.core.CoreActivity


/**
 * The MainActivity Class responsible for holding the user interface
 */

class MainActivity : CoreActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.bottomNavView.setupWithNavController(navController)
    }

}