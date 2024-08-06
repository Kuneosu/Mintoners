package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kuneosu.mintoners.data.model.Notice
import com.kuneosu.mintoners.data.remote.api.GitHubApiService
import com.kuneosu.mintoners.data.remote.api.GitHubFile
import com.kuneosu.mintoners.data.remote.api.RetrofitClient
import com.kuneosu.mintoners.databinding.FragmentNoticeBinding
import com.kuneosu.mintoners.ui.adapter.NoticeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

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
        fetchNotices()

        binding.noticeRecyclerView.adapter = adapter
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.noticeBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun fetchNotices() {
        binding.noticeLoadingAnimation.visibility = View.VISIBLE
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
                binding.noticeLoadingAnimation.visibility = View.GONE
                Toast.makeText(requireContext(), "공지사항을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
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
                _binding?.let { binding ->
                    binding.noticeLoadingAnimation.visibility = View.GONE
                    if (response.isSuccessful) {
                        val file = response.body()
                        val content = String(Base64.decode(file?.content, Base64.DEFAULT))
                        val notice = Gson().fromJson(content, Notice::class.java)
                        noticeList.add(notice)
                        sortNoticesByDate()
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            private fun sortNoticesByDate() {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                noticeList.sortByDescending { notice ->
                    dateFormat.parse(notice.noticeDate)?.time ?: 0L
                }
            }

            override fun onFailure(call: Call<GitHubFile>, t: Throwable) {
                _binding?.let { binding ->
                    binding.noticeLoadingAnimation.visibility = View.GONE
                    Toast.makeText(requireContext(), "공지사항을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
