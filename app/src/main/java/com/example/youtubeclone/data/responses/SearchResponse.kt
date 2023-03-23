package com.example.youtubeclone.data.responses

data class SearchResponse(
    val continuation: String,
    val `data`: List<Data>,
    val estimatedResults: String,
    val msg: String,
    val refinements: List<String>
)