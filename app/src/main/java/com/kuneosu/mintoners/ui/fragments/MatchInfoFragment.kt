package com.kuneosu.mintoners.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.kuneosu.mintoners.R
import com.kuneosu.mintoners.databinding.FragmentMatchInfoBinding


class MatchInfoFragment : Fragment() {
    private var _binding: FragmentMatchInfoBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchInfoBinding.inflate(inflater, container, false)

        binding.matchInfoGameTypeInput.setOnCheckedChangeListener { group, checkedId ->
            matchTypeRadioChanged(checkedId)
        }

        binding.matchInfoGameCountMinus.setOnClickListener {
            val currentCount = binding.matchInfoGameCountNumber.text.toString().toInt()
            if (currentCount > 1) {
                binding.matchInfoGameCountNumber.text = (currentCount - 1).toString()
            }
        }

        binding.matchInfoGameCountPlus.setOnClickListener {
            val currentCount = binding.matchInfoGameCountNumber.text.toString().toInt()
            binding.matchInfoGameCountNumber.text = (currentCount + 1).toString()
        }

        return binding.root
    }

    private fun matchTypeRadioChanged(checkedId: Int) {
        when (checkedId) {
            binding.matchInfoGameTypeDouble.id -> {
                binding.matchInfoGameTypeDouble.setTextColor(Color.WHITE)
                binding.matchInfoGameTypeSingle.setTextColor(
                    resources.getColor(
                        R.color.main,
                        null
                    )
                )
            }

            binding.matchInfoGameTypeSingle.id -> {
                binding.matchInfoGameTypeDouble.setTextColor(
                    resources.getColor(
                        R.color.main,
                        null
                    )
                )
                binding.matchInfoGameTypeSingle.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}