package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchMainRankBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchMainRankFragment : Fragment() {
    private var _binding: FragmentMatchMainRankBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchMainRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}