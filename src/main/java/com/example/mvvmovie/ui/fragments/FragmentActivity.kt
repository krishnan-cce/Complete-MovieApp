package com.example.mvvmovie.ui.fragments

import com.example.mvvmovie.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmovie.databinding.ActivityFragmentBinding
import com.example.mvvmovie.databinding.ActivityMainBinding
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.network.RetrofitClient
import com.example.mvvmovie.ui.MovieViewModel
import com.example.mvvmovie.ui.MovieViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class FragmentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController= navHostFragment.findNavController()

        binding.bottomNavigationView.setupWithNavController(navController)

    }


}