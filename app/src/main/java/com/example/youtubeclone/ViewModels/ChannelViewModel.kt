package com.example.youtubeclone.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.data.responses.SubscribedChannels
import com.example.youtubeclone.repository.FirebaseRespository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val firebaseUser: FirebaseUser?,
    private val firebasereposiotry: FirebaseRespository
) : ViewModel() {
    var currentuser = mutableStateOf(firebaseUser)
    var isLoading = mutableStateOf(false)
    private var _subscribedlist: MutableStateFlow<List<SubscribedChannels>> =
        MutableStateFlow(listOf<SubscribedChannels>())
    var subscribedlist = _subscribedlist.asStateFlow()

    init {
        if (firebaseUser!=null){
            viewModelScope.launch {
                isLoading.value = true
                _subscribedlist.value = firebasereposiotry.getChannelList()
                isLoading.value = false
                Log.d("in th channelview model", " " + _subscribedlist.value)
            }
        }


    }

    fun deleteitem(subscribedChannels: SubscribedChannels) {
        viewModelScope.launch {
            firebasereposiotry.removeChannelList(subscribedChannels)

        }


    }
    fun insertitem(subscribedChannels: SubscribedChannels) {
        viewModelScope.launch {
            firebasereposiotry.addChannelList(subscribedChannels)

        }

    }


}