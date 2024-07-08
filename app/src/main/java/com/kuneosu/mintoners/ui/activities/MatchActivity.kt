package com.kuneosu.mintoners.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivityMatchBinding

class MatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_match)

    }
}