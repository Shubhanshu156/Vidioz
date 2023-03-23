package com.example.youtubeclone.data.responses.autocomplete

data class AutoCompleteResponse(
    val query: String,
    val results: List<String>
)