package com.example.youtubeclone.ViewModels

//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.example.youtubeclone.data.responses.Data
import com.example.youtubeclone.data.responses.SubscribedChannels
import com.example.youtubeclone.data.responses.video.VideoDetails
import com.example.youtubeclone.repository.FirebaseRespository
import com.example.youtubeclone.repository.YoutubeRepository
import com.example.youtubeclone.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class VideoScreenViewModel @Inject constructor(
    val user: FirebaseUser?,
    private val repository: YoutubeRepository,
    private val firebasereposiotry: FirebaseRespository,
    val player: Player
) : ViewModel() {


    var VideoId = mutableStateOf("")
    var issubscribed = mutableStateOf(false)
    var qualityurls = mutableStateMapOf<String, String>()
    var token = mutableStateMapOf<String, String>()
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)


    private var _searchresult: MutableStateFlow<List<Data>> = MutableStateFlow(listOf<Data>())
    var responseresult = _searchresult.asStateFlow()
    private var _videodetails: MutableStateFlow<VideoDetails> = MutableStateFlow(VideoDetails())
    var videoDetails = _videodetails.asStateFlow()
    private var _subscribedvideo: MutableStateFlow<SubscribedChannels> =
        MutableStateFlow(SubscribedChannels())
    var subscribedvideo = _subscribedvideo.asStateFlow()

    fun getRelatedVideos(videoid: String) {

        if (!token.contains(videoid)) {
            viewModelScope.launch {
                isLoading.value = true
                var result = repository.getRelatedVideos(videoid)

                when (result) {
                    is Resource.Success -> {
                        _searchresult.value = result.data?.data!!.filter {
                            it.channelThumbnail != null
                            it.publishedTimeText != null

                        }
                        token[videoid] = result.data!!.continuation
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

//    fun addsavedvideo(){
//        viewModelScope.launch {
//            firebasereposiotry.insertsavedvideo(subscribedvideo.value.)
//        }
//    }

    fun loadmorevideo(videoid: String) {

        viewModelScope.launch {

            var result = repository.getRelatedVideos(videoid, token[videoid])

            when (result) {
                is Resource.Success -> {
                    _searchresult.value += result.data?.data!!.filter {
                        it.channelThumbnail != null
                        it.publishedTimeText != null

                    }
                    token[videoid] = result.data!!.continuation
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

    fun insertitem(subscribedChannels: SubscribedChannels) {
        viewModelScope.launch {
            firebasereposiotry.addChannelList(_subscribedvideo.value)

        }

    }

    fun deleteitem(subscribedChannels: SubscribedChannels) {
        viewModelScope.launch {
            firebasereposiotry.removeChannelList(subscribedChannels)

        }


    }

    fun getvideolinks(quality: String, videoid: String) {
        VideoId.value = videoid
        viewModelScope.launch {
            isLoading.value = true
            loadError.value = ""

            // Get video details
            val videoResult = repository.getVideoDetails(videoid)
            when (videoResult) {
                is Resource.Success -> {
                    videoResult.data?.formats?.forEach { format ->
                        qualityurls[format.qualityLabel] = format.url
                    }
                    if (user != null) {
                        issubscribed.value =firebasereposiotry.IschannelSubscribed(videoResult.data?.channelId!!)
                        Log.d(
                            "value of issubscribed",
                            "getvideolinks: " + issubscribed.value
                        )
                    }

                    val channelResult = repository.getChannelDetails(videoResult.data?.channelId!!)
                    when (channelResult) {
                        is Resource.Success -> {
                            if (!channelResult.data?.channelHandle.isNullOrEmpty()) {
                                    val subscribedChannels = SubscribedChannels(
                                        channelResult.data?.channelHandle!!,
                                        channelResult.data?.avatar?.last()?.url!!,
                                        channelResult.data.subscriberCountText,
                                        channelResult.data?.videosCount!!,
                                        channelResult.data?.channelId!!
                                    )
                                    Log.d(
                                        "value of issubscribed not user login",
                                        "getvideolinks: " + issubscribed.value
                                    )
                                    _subscribedvideo.value = subscribedChannels

                                }





                        }
                        is Resource.Error -> {
                            loadError.value = channelResult.message!!
                        }
                        else -> {}
                    }
                }
                is Resource.Error -> {
                    loadError.value = videoResult.message!!
                }
                else -> {}
            }

            isLoading.value = false
        }

    }

    fun getVideoDetails(videoid: String) {

        viewModelScope.launch {
            isLoading.value = true

            var result = repository.getSearchResults(videoid)
            when (result) {
                is Resource.Success -> {
                    _videodetails.value = VideoDetails(
                        result.data?.data?.get(0)?.channelThumbnail!!,
                        result.data?.data?.get(0)?.title!!,
                        result.data?.data?.get(0)?.channelTitle!!,
                        result.data?.data?.get(0)?.viewCount!!,
                        result.data?.data?.get(0)?.channelId!!,
                        result.data?.data?.get(0)?.description!!,
                        result.data?.data?.get(0)?.publishedTimeText!!,


                        )


                }
                is Resource.Error -> {
                    loadError.value = result.message!!


                }
                else -> {}
            }
            isLoading.value = false


        }


    }


}


