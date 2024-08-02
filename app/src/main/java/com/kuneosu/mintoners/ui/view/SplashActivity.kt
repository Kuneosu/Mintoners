package com.kuneosu.mintoners.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kuneosu.mintoners.MainActivity
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        // Define splash_icon and splash_text
        val splashLogo: ImageView = binding.splashLogo
        val splashTitle: TextView = binding.splashTitle
        binding.splashVersion.text = "version ${getString(R.string.version)}"

        val slideUpLogo = AnimationUtils.loadAnimation(this, R.anim.splash_slide_up_logo)
        val slideUpTitle = AnimationUtils.loadAnimation(this, R.anim.splash_slide_up_title)

        splashLogo.visibility = View.VISIBLE
        splashLogo.startAnimation(slideUpLogo)

        splashTitle.visibility = View.VISIBLE
        splashTitle.startAnimation(slideUpTitle)

        slideUpTitle.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }


}