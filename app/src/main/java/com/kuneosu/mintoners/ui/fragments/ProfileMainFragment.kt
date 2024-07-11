package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentProfileMainBinding


class ProfileMainFragment : Fragment() {

    private var _binding: FragmentProfileMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMainBinding.inflate(inflater, container, false)


        binding.profileKakaoLogin.setOnClickListener {
            binding.profileMainGuest.visibility = View.GONE
            binding.profileMainMember.visibility = View.VISIBLE
        }

        binding.profileNaverLogin.setOnClickListener {
            findNavController().navigate(R.id.action_profileMainFragment_to_profileRegisterFragment)
        }

        binding.profileSettingLogout.setOnClickListener {
            binding.profileMainMember.visibility = View.GONE
            binding.profileMainGuest.visibility = View.VISIBLE
        }

        binding.profileSettingPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_profileMainFragment_to_profilePersonalFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}