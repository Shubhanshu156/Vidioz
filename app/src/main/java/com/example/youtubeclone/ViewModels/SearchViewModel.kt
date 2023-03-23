package com.example.youtubeclone.ViewModels

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.data.HistoryVideo
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.repository.FirebaseRespository
import com.example.youtubeclone.repository.YoutubeRepository
import com.example.youtubeclone.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firebaseRespository: FirebaseRespository,
    private val firebaseUser: FirebaseUser?,
    private val repository: YoutubeRepository
) : ViewModel() {
    var token = mutableStateMapOf<String, String>()
    var loadError = mutableStateOf("")
    var prevquery = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        getSearchResult("AllVideos")
    }

    private var _searchresult: MutableStateFlow<List<Data>> = MutableStateFlow(listOf<Data>())
    var responseresult = _searchresult.asStateFlow()


    fun getSearchResult(query: String) {

        if (token.contains(query) == false || prevquery.value != query) {
            viewModelScope.launch {
                isLoading.value = true
                var result = repository.getSearchResults(query)

                when (result) {
                    is Resource.Success -> {
                        _searchresult.value = result.data?.data!!.filter {
                            it.channelThumbnail != null
                            it.publishedTimeText != null

                        }
                        token.set(query, result.data!!.continuation)
                        loadError.value = ""
                        isLoading.value = false
                        prevquery.value = query
                    }
                    is Resource.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false;


                    }
                    else -> {}
                }
                isLoading.value = false


            }

        }


    }


    fun inserthistory(item: Data) {
        if (firebaseUser != null) {
            viewModelScope.launch {
                firebaseRespository.inserthistory(
                    HistoryVideo(
                        item.videoId,
                        item.thumbnail.last().url,
                        item.title,
                        item.channelTitle,
                        item.lengthText
                    )
                )
            }
        }

    }

    fun loadmoreresult() {
        viewModelScope.launch {

            var result = repository.getSearchResults(prevquery.value, next = token[prevquery.value])

            when (result) {
                is Resource.Success -> {
                    _searchresult.value += result.data?.data!!.filter {
                        it.channelThumbnail != null
                    }
                    token[prevquery.value] = result.data!!.continuation
                    loadError.value = ""
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false;


                }
                else -> {}
            }
            isLoading.value = false


        }
    }


}
