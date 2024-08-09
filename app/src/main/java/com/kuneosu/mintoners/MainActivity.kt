package com.kuneosu.mintoners

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kuneosu.mintoners.databinding.ActivityMainBinding
import com.kuneosu.mintoners.ui.customview.UpdateDialog
import com.kuneosu.mintoners.ui.view.MatchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    private var isActivityRunning = false
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
        Log.d("MainActivity", "Activity created")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(900)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        fetchRemoteConfig()

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

    private fun fetchRemoteConfig() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show()
            return
        }

        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("Update", "fetch Success, updated: $updated")
                    checkForUpdate()
                } else {
                    Log.d("Update", "fetch Failed", task.exception)
                }
            }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun checkForUpdate() {
        val currentVersion = getString(R.string.version)
        val latestVersion = mFirebaseRemoteConfig.getString("latest_version")

        if (currentVersion != latestVersion) {
            showUpdateDialog()
        }
    }

    private fun showUpdateDialog() {
        val dialog = UpdateDialog()
        dialog.show(supportFragmentManager, "UpdateDialog")
    }


    private fun bottomNavigation(item: MenuItem): Boolean {
        if (item.itemId == binding.mainBottomNav.selectedItemId) {
            return true
        }

        when (binding.mainContainer.findNavController().currentDestination?.id) {
            R.id.homeFragment -> {
                binding.mainContainer.findNavController()
                    .navigate(R.id.action_homeFragment_to_settingsFragment)
            }

            R.id.settingsFragment -> {
                binding.mainContainer.findNavController()
                    .navigate(R.id.action_settingsFragment_to_homeFragment)
            }

            R.id.noticeFragment -> {
                binding.mainContainer.findNavController()
                    .navigate(R.id.action_noticeFragment_to_homeFragment)
            }

        }
        return false
    }

    override fun onResume() {
        super.onResume()
        isActivityRunning = true
    }

    override fun onPause() {
        super.onPause()
        isActivityRunning = false
    }

}