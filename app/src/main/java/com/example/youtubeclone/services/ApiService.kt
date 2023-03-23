package com.example.youtubeclone.services

import com.example.youtubeclone.data.responses.AboutChannel
import com.example.youtubeclone.data.responses.SearchResponse
import com.example.youtubeclone.data.responses.TrendingResponse
import com.example.youtubeclone.data.responses.autocomplete.AutoCompleteResponse
import com.example.youtubeclone.data.responses.video.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers(
        "X-RapidAPI-Key:ddc133bb23msh9fbf09b19fcdd94p19ab2djsn080f0c36a5b6",
        "X-RapidAPI-Host:yt-api.p.rapidapi.com"
    )
    @GET("/search")
    suspend fun getSearchresult(
        @Query("query") query: String,
        @Query("type") type: String,
        @Query("token") token: String?,
        @Query("sort_by") sort_by: String,
    ): SearchResponse

    @Headers(
        "X-RapidAPI-Key:ddc133bb23msh9fbf09b19fcdd94p19ab2djsn080f0c36a5b6",
        "X-RapidAPI-Host:yt-api.p.rapidapi.com"
    )
    @GET("/trending")
    suspend fun getTrending(
        @Query("geo") geo: String,
        @Query("type") type: String,
    ): TrendingResponse

    @Headers(
        "X-RapidAPI-Key:ddc133bb23msh9fbf09b19fcdd94p19ab2djsn080f0c36a5b6",
        "X-RapidAPI-Host:yt-api.p.rapidapi.com"
    )
    @GET("/dl")
    suspend fun getVideoDetails(
        @Query("id") id:String
    ):VideoResponse

    @Headers(
        "X-RapidAPI-Key:ddc133bb23msh9fbf09b19fcdd94p19ab2djsn080f0c36a5b6",
        "X-RapidAPI-Host:yt-api.p.rapidapi.com"
    )
    @GET("/related")
    suspend fun getRelatedVideos(
        @Query("id") id:String,
        @Query("token") token:String?,
    ):SearchResponse

        @Headers(
        "X-RapidAPI-Key:ddc133bb23msh9fbf09b19fcdd94p19ab2djsn080f0c36a5b6",
        "X-RapidAPI-Host:yt-api.p.rapidapi.com"
    )
    @GET("/channel/about")
    suspend fun getChannelInformation(
        @Query("id") id:String,

    ):AboutChannel

}