# Vidioz

This project is a clone of the YouTube application, allowing users to watch, search, and
save videos. The app is built using kotlin , and uses the YouTube API to fetch video data.

## Installation

To install this app, follow these steps:

1. Clone this repository: `git clone https://github.com/Shubhanshu156/Vidioz`
2. Open the project in android Studio
3. Build and run the app on an Android device or emulator.

## Features

This app includes the following features:

- **Search** : Users can search for specific videos, channels, or topics on YouTube.
- **Video Sharing**: YouTube allows users to upload and share videos with others.
- **History**: User History is saved in order to access previous watced video faster
- **Trending**: Get videos that are trending in your region
- **Download**: Download Any video you want in different quality

## Screenshots

<div style="overflow-x: auto;">
    <table>
    <tr>
        <td>
            <h4>Home Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/Vidioz/master/home.gif" height="370" width="200">
        </td>
        <td>
            <h4>Discover Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/Vidioz/master/discover.gif" height="370" width="200">
        </td>
        <td>
            <h4>Search Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/Vidioz/master/search.gif" height="370" width="200">
        </td>
        <td>
            <h4>Channel Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/Vidioz/master/videoplay.gif" height="370" width="200">
        </td>
        <td>
            <h4>Channel Screen Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/Vidioz/master/channel.gif" height="370" width="200">
        </td>
        <td>
            <h4>History Screen</h4>
            <img src="https://raw.githubusercontent.com/Shubhanshu156/Vidioz/master/history.gif" height="370" width="200">
        </td>
    </tr>
    </table>
</div>

## Credits

This application design was taken from [soroush norozy](https://dribbble.com/soroushnrz) design on dribble .I would like to thanks [soroush norozy](https://dribbble.com/soroushnrz) for such amazing design.
You can check out his design [here](https://dribbble.com/shots/19387285-YouTube-App-Redesign-Concept)

## Libraries Used in Project

- [JetPack Compose](https://developer.android.com/jetpack/compose?gclsrc=ds&gclsrc=ds)-Jetpack Compose is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.
  - [JetPack Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - The Navigation component provides support for Jetpack Compose applications. You can navigate between composables while taking advantage of the Navigation component’s infrastructure and features.
  - [Jetpack compose States](https://developer.android.com/jetpack/compose/state) - In Jetpack Compose, states are objects that can hold and manage the current value of a piece of data. When a state value changes, Compose automatically recomposes any UI elements that depend on that value, updating the display with the new value
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.

  - [Flows](https://developer.android.com/kotlin/flow) -Flows are built on top of coroutines and can provide multiple values. A flow is conceptually a stream of data that can be computed asynchronously.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.

- [Dagger-Hilt](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.

- [Coil-kt](https://coil-kt.github.io/coil/compose/) - An image loading library for Android backed by Kotlin Coroutines.
- [Firebase](https://firebase.google.com/)
  - [Firebase Firestore](https://firebase.google.com/docs/firestore)- Cloud Firestore is a flexible, scalable database for mobile, web, and server development from Firebase and Google Cloud. Like Firebase Realtime Database
  - [Firebase Authentication](https://firebase.google.com/docs/auth/android/start)- Firebase Authentication provides backend services, easy-to-use SDKs, and ready-made UI libraries to authenticate users to your app.
