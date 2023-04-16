package com.example.youtubeclone.services

import com.example.youtubeclone.data.responses.autocomplete.AutoCompleteResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AutoCompleteService {

    @Headers(
        "X-RapidAPI-Key:Enter Your Key here",
        "X-RapidAPI-Host:youtube138.p.rapidapi.com"
    )
    @GET("/auto-complete/")
    suspend fun getSuggestion(
        @Query("q") q: String,
    ): AutoCompleteResponse
}
