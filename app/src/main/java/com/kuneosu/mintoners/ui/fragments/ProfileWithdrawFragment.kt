package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentProfileWithdrawBinding

class ProfileWithdrawFragment : Fragment() {
    private var _binding: FragmentProfileWithdrawBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileWithdrawBinding.inflate(inflater, container, false)

        binding.profileWithdrawWithdrawButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileWithdrawFragment_to_profileMainFragment)
        }

        binding.profileWithdrawBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}