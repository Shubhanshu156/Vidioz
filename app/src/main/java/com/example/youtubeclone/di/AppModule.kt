package com.example.youtubeclone.di


import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.youtubeclone.R
import com.example.youtubeclone.repository.FirebaseRespository
import com.example.youtubeclone.services.ApiService
import com.example.youtubeclone.repository.YoutubeRepository
import com.example.youtubeclone.services.AutoCompleteService
import com.example.youtubeclone.services.FirebaseService
import com.example.youtubeclone.util.Constants.AUTO_BASE_URL
import com.example.youtubeclone.util.Constants.BASE_URL
import com.example.youtubeclone.util.Constants.CLIENT_ID
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideapi(): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesuggestion(): AutoCompleteService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AUTO_BASE_URL)
            .build()
            .create(AutoCompleteService::class.java)
    }


    @Singleton
    @Provides
    fun provideYoutubeRepo(api: ApiService, autocomplete: AutoCompleteService): YoutubeRepository {
        return YoutubeRepository(api, autocomplete)
    }

    @Singleton
    @Provides
    fun funfirebaserepo(
        db: FirebaseFirestore,
        currentuser: FirebaseUser?
    ): FirebaseRespository {
        return FirebaseRespository(db, currentuser)
    }

    @Provides
    fun providescurrentuser(): FirebaseUser? = Firebase.auth.currentUser

    @Provides
    fun providedb(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideVideoPlayer(app: Application): Player {
        return ExoPlayer.Builder(app).build()
    }
}





