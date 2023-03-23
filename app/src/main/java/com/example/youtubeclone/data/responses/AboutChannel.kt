package com.example.youtubeclone.data.responses

data class AboutChannel(
    val availableCountries: List<String>,
    val avatar: List<Avatar>,
    val banner: List<Banner>,
    val channelHandle: String,
    val channelId: String,
    val country: String,
    val description: String,
    val isFamilySafe: Boolean,
    val isVerified: Boolean,
    val joinedDate: String,
    val keywords: List<String>,
    val links: List<Link>,
    val mobileBanner: List<MobileBanner>,
    val subscriberCount: Int,
    val subscriberCountText: String,
    val tabs: List<String>,
    val title: String,
    val tvBanner: List<TvBanner>,
    val videosCount: Int,
    val videosCountText: String,
    val viewCount: String
)