package com.example.youtubeclone.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class navitem(
    val name:String,
    val route:String,
    val icon:Painter
)
