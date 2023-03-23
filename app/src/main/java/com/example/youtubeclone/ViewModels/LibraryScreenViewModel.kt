package com.example.youtubeclone.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.data.HistoryVideo
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.repository.FirebaseRespository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryScreenViewModel @Inject constructor(
    private val firebaseUser: FirebaseUser?,

    private val firebaseRespository: FirebaseRespository
) :
    ViewModel() {
    var currentuser = mutableStateOf(firebaseUser)
    var isLoading= mutableStateOf(false)
    private var _history: MutableStateFlow<List<HistoryVideo>> = MutableStateFlow(listOf<HistoryVideo>())
    var history = _history.asStateFlow()
    private var _savedvideo: MutableStateFlow<List<HistoryVideo>> = MutableStateFlow(listOf<HistoryVideo>())
    var saved = _savedvideo.asStateFlow()
    init {
        isLoading.value=true
        viewModelScope.launch {
            _history.value=firebaseRespository.getallHistory()
//            _savedvideo.value=firebaseRespository.getAllSavedVideo()
            isLoading.value=false
        }

    }
}
