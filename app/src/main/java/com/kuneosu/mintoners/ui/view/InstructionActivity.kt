package com.kuneosu.mintoners.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.ActivityInstructionBinding
import com.kuneosu.mintoners.databinding.ActivityMatchBinding

class InstructionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstructionBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // JavaScript 및 DOM Storage 활성화
        binding.instructionWebview.settings.javaScriptEnabled = true
        binding.instructionWebview.settings.domStorageEnabled = true

        // WebViewClient 설정
        binding.instructionWebview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false // 모든 URL을 WebView에서 로드
            }
        }


        binding.instructionWebview.loadUrl("https://kimkwonsu.notion.site/Mintoners-940634d9973a41c18a230a93a3b98adc?pvs=4")
    }
}