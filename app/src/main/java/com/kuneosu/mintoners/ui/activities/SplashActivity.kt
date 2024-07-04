package com.kuneosu.mintoners.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kuneosu.mintoners.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Define splash_icon and splash_text
        val splashLogo: ImageView = findViewById(R.id.splash_logo)
        val splashTitle: TextView = findViewById(R.id.splash_title)

        val slideUpLogo = AnimationUtils.loadAnimation(this, R.anim.splash_slide_up_logo)
        val slideUpTitle = AnimationUtils.loadAnimation(this, R.anim.splash_slide_up_title)

        splashLogo.visibility = View.VISIBLE
        splashLogo.startAnimation(slideUpLogo)

        splashTitle.visibility = View.VISIBLE
        splashTitle.startAnimation(slideUpTitle)

        slideUpTitle.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}