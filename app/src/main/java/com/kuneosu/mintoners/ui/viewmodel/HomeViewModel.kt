package com.kuneosu.mintoners.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuneosu.mintoners.data.model.Match
import com.kuneosu.mintoners.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository

) : ViewModel() {
    private val _matches = MutableLiveData<List<Match>>()
    val matches: LiveData<List<Match>> get() = _matches


    fun deleteAllMatches() {
        repository.deleteAllMatches()
    }

    fun deleteMatchByNumber(number: Int) {
        repository.deleteMatchByNumber(number)
    }

    fun getAllMatches() {
        viewModelScope.launch {
            _matches.value = repository.getAllMatchesList()
            Log.d(TAG, "getAllMatches: ${repository.getAllMatchesList()}")
            Log.d(TAG, "getAllMatches: ${_matches.value}")
        }
    }

    fun insertMatch(match: Match) {
        repository.insertMatch(match)
    }

    fun updateMatchByNumber(number: Int, match: Match) {
        repository.updateMatchByNumber(number, match)
    }
}