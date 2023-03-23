package com.example.youtubeclone.services

import com.example.youtubeclone.data.responses.autocomplete.AutoCompleteResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AutoCompleteService {

    @Headers(
        "X-RapidAPI-Key:877e131c07msh18590c4f457c4bbp1e2d50jsn0e1a3c9c4781",
        "X-RapidAPI-Host:youtube138.p.rapidapi.com"
    )
    @GET("/auto-complete/")
    suspend fun getSuggestion(
        @Query("q") q: String,
    ): AutoCompleteResponse
}