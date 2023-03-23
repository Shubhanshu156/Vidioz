package com.example.youtubeclone.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.repository.YoutubeRepository
import com.example.youtubeclone.services.ApiService
import com.example.youtubeclone.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestionScreenViewModel @Inject constructor(val repository: YoutubeRepository):ViewModel() {
    private var _searchresult: MutableStateFlow<List<String>> = MutableStateFlow(listOf<String>())
    var responseresult = _searchresult.asStateFlow()
    var isLoading= mutableStateOf(false)
    var loadError= mutableStateOf("")

    fun giveSuggestion(q:String){
        if(q.isEmpty()){
            _searchresult.value= listOf()
        }
        else{ viewModelScope.launch {
            isLoading.value = true
            var result = repository.getSuggestion(q)

            when (result) {
                is Resource.Success -> {
                    _searchresult.value = result.data?.results!!

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


        }}

    }
}