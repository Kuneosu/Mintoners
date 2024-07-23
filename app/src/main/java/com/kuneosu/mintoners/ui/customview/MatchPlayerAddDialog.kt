package com.kuneosu.mintoners.ui.customview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.kuneosu.mintoners.data.model.Player
import com.kuneosu.mintoners.databinding.MatchInfoDialogBinding
import com.kuneosu.mintoners.databinding.MatchPlayerAddDialogBinding
import com.kuneosu.mintoners.ui.viewmodel.MatchViewModel

class MatchPlayerAddDialog(

) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: MatchPlayerAddDialogBinding? = null
    private val binding get() = _binding!!
    private val matchViewModel: MatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MatchPlayerAddDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.playerAddDialogAdd.setOnClickListener {
            val players = binding.playerAddDialogEdittext.text.toString().split(" ")
            val playerList = mutableListOf<Player>()
            var playerIndex = matchViewModel.players.value?.size?.plus(1) ?: 1
            for (player in players) {
                playerList.add(Player(playerIndex = playerIndex, playerName = player))
                playerIndex += 1
            }
            matchViewModel.addPlayerList(playerList)
            dismiss()
        }

        // 취소 버튼 클릭
        binding.playerAddDialogClose.setOnClickListener {
            dismiss()
        }


        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
