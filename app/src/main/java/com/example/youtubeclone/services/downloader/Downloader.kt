package com.example.youtubeclone.services.downloader

interface Downloader {
fun downloadFile(url:String,filename:String):Long
}