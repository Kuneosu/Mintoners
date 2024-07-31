package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Notice
import com.kuneosu.mintoners.data.remote.api.GitHubApiService
import com.kuneosu.mintoners.data.remote.api.GitHubFile
import com.kuneosu.mintoners.data.remote.api.RetrofitClient
import com.kuneosu.mintoners.databinding.FragmentNoticeBinding
import com.kuneosu.mintoners.ui.adapter.NoticeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NoticeFragment : Fragment() {
    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NoticeAdapter
    private val noticeList = mutableListOf<Notice>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)

        adapter = NoticeAdapter()
        adapter.submitList(noticeList)
        binding.noticeRecyclerView.adapter = adapter
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchNotices()

        binding.noticeBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun fetchNotices() {
        val apiService = RetrofitClient.getClient("https://api.github.com/")
            .create(GitHubApiService::class.java)
        val call = apiService.getFiles("Kuneosu", "Mintoners", "Notices")
        call.enqueue(object : Callback<List<GitHubFile>> {
            override fun onResponse(
                call: Call<List<GitHubFile>>,
                response: Response<List<GitHubFile>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.forEach { file ->
                        fetchFileContent(file.name)
                    }
                }
            }

            override fun onFailure(call: Call<List<GitHubFile>>, t: Throwable) {
                Log.d("NoticeFragment", "onFailure: ${t.message}")
            }
        })
    }

    private fun fetchFileContent(fileName: String) {
        val apiService = RetrofitClient.getClient("https://api.github.com/")
            .create(GitHubApiService::class.java)
        val call = apiService.getFileContent("Kuneosu", "Mintoners", "Notices/$fileName")
        call.enqueue(object : Callback<GitHubFile> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<GitHubFile>, response: Response<GitHubFile>) {
                if (response.isSuccessful) {
                    val file = response.body()
                    val content = String(Base64.decode(file?.content, Base64.DEFAULT))
                    val notice = Gson().fromJson(content, Notice::class.java)
                    noticeList.add(notice)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<GitHubFile>, t: Throwable) {
                Log.d("NoticeFragment", "onFailure: ${t.message}")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}