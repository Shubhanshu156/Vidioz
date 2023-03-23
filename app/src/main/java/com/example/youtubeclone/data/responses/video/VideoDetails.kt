package com.example.youtubeclone.data.responses.video

import com.example.youtubeclone.data.responses.ChannelThumbnail

data class VideoDetails(
    val ChannelThumbnail: List<ChannelThumbnail>,
    val VideoTittle: String,
    val ChannelName: String,
    val views: String,
    val ChannelId: String,
    val description: String,
    val time:String


    ) {
    constructor(
    ):this(listOf(),"","","","","","")
}
