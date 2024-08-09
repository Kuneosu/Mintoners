package com.kuneosu.mintoners.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentSettingsBinding
import com.kuneosu.mintoners.ui.customview.FeedbackDialog


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        settingFragmentDisplaySetting()

        return binding.root
    }

    private fun settingFragmentDisplaySetting() {
        binding.settingsVersion.text = getString(R.string.version)

        binding.settingsSettingItem.setOnClickListener {
            Toast.makeText(requireContext(), "아직 변경 가능한 설정이 없습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.settingsFeedbackItem.setOnClickListener {
            val dialog = FeedbackDialog()
            dialog.show(childFragmentManager, "FeedbackDialog")
        }

        binding.settingsNoticeItem.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_noticeFragment)
        }

        binding.settingBackButton.setOnClickListener {
            updateBottomNavigationView(R.id.menu_home)
        }
    }

    private fun updateBottomNavigationView(menuItemId: Int) {
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.main_bottom_nav)
        bottomNavigationView?.selectedItemId = menuItemId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}