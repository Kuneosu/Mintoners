package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchInfoBinding


class MatchInfoFragment : Fragment() {
    private lateinit var binding: FragmentMatchInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

}