package com.example.youtubeclone.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.repository.YoutubeRepository
import com.example.youtubeclone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrendingViewModel @Inject constructor(private val repository: YoutubeRepository) :
    ViewModel() {

    var loadError = mutableStateOf("")
    var prevquery = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var _searchresult: MutableStateFlow<List<Data>> = MutableStateFlow(listOf<Data>())
    var responseresult = _searchresult.asStateFlow()

    init {
        getTrendingResults("now", "IN")
    }

    fun getTrendingResults(type: String, geo: String) {

        viewModelScope.launch {
            isLoading.value = true
            var result = repository.getTrending(type=type, geo=geo)

            when (result) {
                is Resource.Success -> {
                    _searchresult.value = result.data?.data!!.filter {
                        it.channelThumbnail != null
                    }
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
