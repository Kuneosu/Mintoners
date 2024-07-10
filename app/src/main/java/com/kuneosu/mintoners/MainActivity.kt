package com.kuneosu.mintoners

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.kuneosu.mintoners.databinding.ActivityMainBinding
import com.kuneosu.mintoners.ui.view.MatchActivity
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
                binding.mainContainer.findNavController().popBackStack()
                if (binding.mainContainer.findNavController().currentDestination?.id == R.id.homeFragment) {
                    binding.mainBottomNav.menu.getItem(0).isChecked = true
                }
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
            // start MatchActivity
            val intent = Intent(this, MatchActivity::class.java)
            startActivity(intent)
        }


        // 하단 앱 바 클릭 이벤트 처리
        binding.mainBottomNav.setOnItemSelectedListener { item ->
            if (bottomNavigation(item)) return@setOnItemSelectedListener false
            true
        }

        // 뒤로가기 버튼을 처리하는 콜백 추가
        onBackPressedDispatcher.addCallback(this, callback)

    }

    private fun bottomNavigation(item: MenuItem): Boolean {
        if (item.itemId == binding.mainBottomNav.selectedItemId) {
            return true
        }

        when (binding.mainContainer.findNavController().currentDestination?.id) {
            R.id.homeFragment -> {
                binding.mainContainer.findNavController()
                    .navigate(R.id.action_homeFragment_to_profileMainFragment)
            }

            R.id.profileMainFragment -> {
                binding.mainContainer.findNavController()
                    .navigate(R.id.action_profileMainFragment_to_homeFragment)
            }

            R.id.profileRegisterFragment -> {
                when (item.itemId) {
                    R.id.menu_home -> {
                        binding.mainContainer.findNavController()
                            .navigate(R.id.action_profileRegisterFragment_to_homeFragment)
                    }

                    R.id.menu_profile -> {
                        binding.mainContainer.findNavController()
                            .navigate(R.id.action_profileRegisterFragment_to_profileMainFragment)
                    }
                }
            }

            R.id.profilePersonalFragment -> {
                when (item.itemId) {
                    R.id.menu_home -> {
                        binding.mainContainer.findNavController()
                            .navigate(R.id.action_profilePersonalFragment_to_homeFragment)
                    }

                    R.id.menu_profile -> {
                        binding.mainContainer.findNavController()
                            .navigate(R.id.action_profilePersonalFragment_to_profileMainFragment)
                    }
                }
            }

            R.id.profileWithdrawFragment -> {
                when (item.itemId) {
                    R.id.menu_home -> {
                        binding.mainContainer.findNavController()
                            .navigate(R.id.action_profileWithdrawFragment_to_homeFragment)
                    }

                    R.id.menu_profile -> {
                        binding.mainContainer.findNavController()
                            .navigate(R.id.action_profileWithdrawFragment_to_profileMainFragment)
                    }
                }
            }
        }
        return false
    }


}