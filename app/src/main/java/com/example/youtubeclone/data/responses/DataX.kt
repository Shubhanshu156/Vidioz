package com.example.youtubeclone.data.responses

data class DataX(
    val isOriginalAspectRatio: Boolean,
    val params: String,
    val playerParams: String,
    val query: String,
    val sequenceParams: String,
    val thumbnail: List<ThumbnailX>,
    val title: String,
    val type: String,
    val videoId: String,
    val viewCountText: String
)