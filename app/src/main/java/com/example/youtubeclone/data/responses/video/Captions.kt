package com.example.youtubeclone.data.responses.video

data class Captions(
    val captionTracks: List<CaptionTrack>,
    val translationLanguages: List<TranslationLanguage>
)