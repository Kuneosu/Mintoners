package com.kuneosu.mintoners.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuneosu.mintoners.data.model.Notice
import com.kuneosu.mintoners.databinding.NoticeItemBinding

class NoticeAdapter :
    ListAdapter<Notice, NoticeAdapter.NoticeViewHolder>(diffUtil) {
    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<Notice>() {
            override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
                return oldItem.noticeTitle == newItem.noticeTitle
            }

            override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var noticeToggle = false

    override fun getItemCount(): Int {
        return currentList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding =
            NoticeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class NoticeViewHolder(private val binding: NoticeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind(notice: Notice) {
            binding.noticeTitle.text = notice.noticeTitle

            binding.noticeItem.setOnClickListener {
                noticeToggle = !noticeToggle
                notifyItemChanged(adapterPosition)
            }

            if (noticeToggle) {
                binding.noticeBody.visibility = View.VISIBLE
                binding.noticeToggle.rotation = 90f
            } else {
                binding.noticeBody.visibility = View.GONE
                binding.noticeToggle.rotation = -90f
            }

            if (binding.noticeBody.visibility == View.VISIBLE) {
                binding.noticeContent.text = notice.noticeContent
            }

        }

    }
}