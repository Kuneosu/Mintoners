package com.kuneosu.mintoners.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )



        binding.mainBottomNav.background = null
        binding.mainBottomNav.menu.getItem(1).isEnabled = false

        binding.mainFloatingAddBtn.setOnClickListener {
            Toast.makeText(this, "Add Button Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.mainBottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == binding.mainBottomNav.selectedItemId) {
                return@setOnItemSelectedListener false
            }
            when (item.itemId) {
                R.id.menu_home -> {
                    binding.mainContainer.findNavController().navigate(R.id.action_profileMainFragment_to_homeFragment)
                }
                R.id.menu_profile -> {
                    binding.mainContainer.findNavController().navigate(R.id.action_homeFragment_to_profileMainFragment)
                }
            }
            true
        }
    }

}