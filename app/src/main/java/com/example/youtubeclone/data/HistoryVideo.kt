package com.example.youtubeclone.data

data class HistoryVideo(
    val videoid:String,
    val videothumbnail:String,
    val videotittle:String,
    val channelname:String,
    val videotime:String
)
{
    constructor():this("","","","","")
}