package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.data.model.Notice
import com.kuneosu.mintoners.databinding.FragmentNoticeBinding
import com.kuneosu.mintoners.ui.adapter.NoticeAdapter


class NoticeFragment : Fragment() {
    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)

        val noticeList = listOf(
            Notice(
                "안녕하세요. Mintoners입니다.",
                "안녕하세요 Mintoners입니다.\n저희 서비스의 시작에 함께 해주셔서 감사합니다.",
                "2024-07-31"
            ),
        )
        val noticeAdapter = NoticeAdapter()
        noticeAdapter.submitList(noticeList)
        binding.noticeRecyclerView.adapter = noticeAdapter
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.noticeBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}