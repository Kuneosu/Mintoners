package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentProfileRegisterBinding


class ProfileRegisterFragment : Fragment() {
    private var _binding: FragmentProfileRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileRegisterBinding.inflate(inflater, container, false)

        binding.profileRegisterRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileRegisterFragment_to_profileMainFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}