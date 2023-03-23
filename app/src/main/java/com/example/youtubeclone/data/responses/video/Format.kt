package com.example.youtubeclone.data.responses.video

data class Format(
    val approxDurationMs: String,
    val audioChannels: Int,
    val audioSampleRate: String,
    val averageBitrate: Int,
    val bitrate: Int,
    val contentLength: String,
    val fps: Int,
    val height: Int,
    val itag: Int,
    val mimeType: String,
    val projectionType: String,
    val quality: String,
    val qualityLabel: String,
    val url: String,
    val width: Int
)