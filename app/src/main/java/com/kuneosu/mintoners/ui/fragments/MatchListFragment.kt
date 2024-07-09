package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchListBinding


class MatchListFragment : Fragment() {
    private var _binding: FragmentMatchListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchListBinding.inflate(inflater, container, false)


        return binding.root
    }

}