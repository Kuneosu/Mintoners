package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentProfilePersonalBinding

class ProfilePersonalFragment : Fragment() {
    private var _binding: FragmentProfilePersonalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePersonalBinding.inflate(inflater, container, false)

        binding.profilePersonalBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.profilePersonalSaveText.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.profilePersonalSaveButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.profilePersonalWithdrawButton.setOnClickListener {
            findNavController().navigate(R.id.action_profilePersonalFragment_to_profileWithdrawFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}