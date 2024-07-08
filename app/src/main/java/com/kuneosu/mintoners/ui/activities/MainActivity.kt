package com.kuneosu.mintoners.ui.activities

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var isDouble = false

    private fun backPressToast() {
        Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.mainContainer.findNavController().currentDestination?.id == R.id.homeFragment) {
                when {
                    isDouble -> {
                        finish()
                    }
                }
                backPressToast()

                isDouble = true
                Handler().postDelayed({
                    isDouble = false
                }, 2000)
            } else {
                binding.mainBottomNav.selectedItemId = R.id.menu_home
            }
        }
    }


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
                    binding.mainContainer.findNavController()
                        .navigate(R.id.action_profileMainFragment_to_homeFragment)
                }

                R.id.menu_profile -> {
                    binding.mainContainer.findNavController()
                        .navigate(R.id.action_homeFragment_to_profileMainFragment)
                }
            }
            true
        }


        // 뒤로가기 버튼을 처리하는 콜백 추가
        onBackPressedDispatcher.addCallback(this, callback)

    }


}