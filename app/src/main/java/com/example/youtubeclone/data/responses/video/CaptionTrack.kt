package com.example.youtubeclone.data.responses.video

data class CaptionTrack(
    val baseUrl: String,
    val isTranslatable: Boolean,
    val languageCode: String,
    val name: String,
    val vssId: String
)