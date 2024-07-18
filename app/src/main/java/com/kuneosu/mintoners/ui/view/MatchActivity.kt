package com.kuneosu.mintoners.ui.view

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivityMatchBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchBinding
    private val matchViewModel: MatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 넘겨받은 MatchNumber가 있을 경우 기존 데이터 로딩
        matchNumberSetting()
    }

    private fun matchNumberSetting() {
        val matchNumber = intent.getIntExtra("matchNumber", 0)
        if (matchNumber != 0) {
            matchViewModel.setMatchNumber(matchNumber)
        }
    }

}