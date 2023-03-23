package com.example.youtubeclone.repository


import com.example.youtubeclone.data.responses.AboutChannel
import com.example.youtubeclone.services.ApiService
import com.example.youtubeclone.data.responses.SearchResponse
import com.example.youtubeclone.data.responses.TrendingResponse
import com.example.youtubeclone.data.responses.autocomplete.AutoCompleteResponse
import com.example.youtubeclone.data.responses.video.VideoResponse
import com.example.youtubeclone.services.AutoCompleteService
import com.example.youtubeclone.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class YoutubeRepository @Inject constructor(
    private val api: ApiService,
    private val autoCompleteService: AutoCompleteService
) {


    suspend fun getSearchResults(query: String, next: String? = null): Resource<SearchResponse> {

        var results = try {
            api.getSearchresult(query, "video", next, sort_by = "relevance")
        } catch (e: Exception) {
            return Resource.Error("an Unknown Error Occurred")
        }
        return Resource.Success(results)
    }
    suspend fun getRelatedVideos(videoid: String, next: String? = null): Resource<SearchResponse> {

        var results = try {
            api.getRelatedVideos(videoid, next)
        } catch (e: Exception) {
            return Resource.Error("an Unknown Error Occurred")
        }
        return Resource.Success(results)
    }

    suspend fun getTrending(type: String, geo: String): Resource<TrendingResponse> {
        var results = try {
            api.getTrending(type = type, geo = geo)
        } catch (e: Exception) {
            return Resource.Error("an Unknown Error Occurred")
        }
        return Resource.Success(results)

    }

    suspend fun getSuggestion(q: String): Resource<AutoCompleteResponse> {
        var results = try {
            autoCompleteService.getSuggestion(q)
        } catch (e: Exception) {
            return Resource.Error("an Unknown Error Occurred")
        }
        return Resource.Success(results)
    }
    suspend fun getVideoDetails(id:String):Resource<VideoResponse>{
        var results = try {
            api.getVideoDetails(id)
        } catch (e: Exception) {
            return Resource.Error("an Unknown Error Occurred")
        }
        return Resource.Success(results)
    }
    suspend fun getChannelDetails(channelid:String):Resource<AboutChannel>{
        var results = try {
            api.getChannelInformation(channelid)
        } catch (e: Exception) {
            return Resource.Error("an Unknown Error Occurred")
        }
        return Resource.Success(results)
    }
}


