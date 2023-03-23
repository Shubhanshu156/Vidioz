package com.example.youtubeclone.data.responses.video

data class VideoResponse(
    val adaptiveFormats: List<AdaptiveFormat>,
    val allowRatings: Boolean,
    val captions: Captions,
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val expiresInSeconds: String,
    val formats: List<Format>,
    val id: String,
    val isLiveContent: Boolean,
    val isPrivate: Boolean,
    val isUnpluggedCorpus: Boolean,
    val keywords: List<String>,
    val lengthSeconds: String,
    val status: String,
    val storyboards: List<Storyboard>,
    val thumbnail: List<Thumbnail>,
    val title: String,
    val viewCount: String
)