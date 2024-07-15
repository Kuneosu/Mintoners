package com.kuneosu.mintoners.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuneosu.mintoners.databinding.ActivityMatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}