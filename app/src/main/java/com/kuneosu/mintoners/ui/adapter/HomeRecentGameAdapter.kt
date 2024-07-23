package com.kuneosu.mintoners.ui.adapter

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.databinding.RecentGameItemBinding
import com.kuneosu.mintoners.ui.view.MatchActivity
import com.kuneosu.mintoners.ui.viewmodel.HomeViewModel
import com.kuneosu.mintoners.util.ItemTouchHelperListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeRecentGameAdapter(private val homeViewModel: HomeViewModel) :
    ListAdapter<Match, HomeRecentGameAdapter.MatchViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Match>() {
            override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
                return oldItem.matchNumber == newItem.matchNumber
            }

            override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding =
            RecentGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class MatchViewHolder(private val binding: RecentGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(match: Match) {
            binding.recentGameTitle.text = match.matchName

            binding.recentGameInfoImage.setOnClickListener {
                homeViewModel.deleteMatchByNumber(match.matchNumber)
                homeViewModel.getAllMatches()
            }

            val outputDateStr: String = dateToString(match)

            // 결과 출력
            binding.recentGameDate.text = outputDateStr

            binding.recentGameCard.setOnClickListener {
                val intent = Intent(binding.root.context, MatchActivity::class.java)
                intent.putExtra("matchNumber", match.matchNumber)
                Log.d("matchState", "matchNumber: ${match.matchNumber}")
                Log.d("matchState", "matchState: ${match.matchState}")
                intent.putExtra("matchState", match.matchState)
                binding.root.context.startActivity(intent)
            }


        }

        private fun dateToString(match: Match): String {
            val inputDateStr = match.matchDate

            // 입력 문자열의 형식을 지정
            val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

            // 입력 문자열을 Date 객체로 변환
            val date: Date? = inputFormat.parse(inputDateStr.toString())

            // 출력 형식을 지정
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            // Date 객체를 지정된 형식의 문자열로 변환
            val outputDateStr: String = outputFormat.format(date!!)
            return outputDateStr
        }
    }

}