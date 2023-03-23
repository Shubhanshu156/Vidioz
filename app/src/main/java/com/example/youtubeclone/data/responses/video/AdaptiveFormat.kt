package com.example.youtubeclone.data.responses.video

data class AdaptiveFormat(
    val approxDurationMs: String,
    val audioChannels: Int,
    val audioQuality: String,
    val audioSampleRate: String,
    val averageBitrate: Int,
    val bitrate: Int,
    val contentLength: String,
    val fps: Int,
    val height: Int,
    val highReplication: Boolean,
    val itag: Int,
    val loudnessDb: Double,
    val mimeType: String,
    val projectionType: String,
    val quality: String,
    val qualityLabel: String,
    val url: String,
    val width: Int
)