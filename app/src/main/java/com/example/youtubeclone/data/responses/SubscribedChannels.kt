package com.example.youtubeclone.data.responses

data class SubscribedChannels(
    val channelname:String,
    val thumbnailurl:String,
    val subscribers: String?,
    val videocount:Int,
    val videoid: String
    ) {
constructor():this("","","",0,"")
}
