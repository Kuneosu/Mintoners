package com.kuneosu.mintoners.repository

import com.kuneosu.mintoners.model.data.RecentGame
import javax.inject.Inject

class HomeRepository @Inject constructor() {

    fun getRecentGames(): List<RecentGame> {
        // 데이터 소스를 통해 데이터를 가져옵니다.
        // 예시 데이터를 반환합니다.
        return listOf(
            RecentGame("대회명1", "2024.06.01", 15, "진행중"),
            RecentGame("대회명2", "2024.06.02", 20, "완료"),
            RecentGame("대회명1대회명1대회명1", "2024.06.01", 15, "진행중"),
            RecentGame("대회명2", "2024.06.02", 20, "완료"),
            RecentGame("대회명1", "2024.06.01", 15, "진행중"),
            RecentGame("대회명2", "2024.06.02", 20, "완료"),
        )
    }
}
