package com.example.youtubeclone.data.responses

data class Data(
    val channelId: String,
    val channelThumbnail: List<ChannelThumbnail>,
    val channelTitle: String,
    val `data`: List<DataX>,
    val description: String,
    val lengthText: String,
    val publishedTimeText: String,
    val richThumbnail: List<RichThumbnail>,
    val thumbnail: List<ThumbnailX>,
    val title: String,
    val type: String,
    val videoId: String,
    val viewCount: String
)