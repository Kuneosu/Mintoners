package com.kuneosu.mintoners.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuneosu.mintoners.model.data.RecentGame
import com.kuneosu.mintoners.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _title = MutableLiveData<String>().apply {
        value = "Home Title"
    }
    val title: LiveData<String> get() = _title

    private val _recentGames = MutableLiveData<List<RecentGame>>()
    val recentGames: LiveData<List<RecentGame>> get() = _recentGames

    init {
        fetchRecentGames()
    }

    private fun fetchRecentGames() {
        _recentGames.value = repository.getRecentGames()
    }
}
