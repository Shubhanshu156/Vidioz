package com.example.youtubeclone.util

fun getFormattedCount(input: String): String {
    val count = input.toLong()

    return when {
        count < 1000 -> {
            "$count"
        }
        count < 1000000 -> {
            val thousands = count / 1000
            val remainder = count % 1000
            if (remainder == 0L) "$thousands" + "K" else "$thousands.${remainder / 100}K"
        }
        count < 1000000000 -> {
            val millions = count / 1000000
            val remainder = count % 1000000
            if (remainder == 0L) "$millions" + "M" else "$millions.${remainder / 100000}M"
        }
        else -> {
            val billions = count / 1000000000
            val remainder = count % 1000000000
            if (remainder == 0L) "$billions" + "B" else "$billions.${remainder / 100000000}B"
        }
    }
}
