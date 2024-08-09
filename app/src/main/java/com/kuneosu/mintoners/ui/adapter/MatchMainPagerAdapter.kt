package com.kuneosu.mintoners.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val TAG = "MatchMainPagerAdapter"

class MatchMainPagerAdapter(
    private val fragmentList: List<Fragment>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}