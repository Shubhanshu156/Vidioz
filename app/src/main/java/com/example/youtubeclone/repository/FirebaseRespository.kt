package com.example.youtubeclone.repository

import android.util.Log
import com.example.youtubeclone.data.HistoryVideo
import com.example.youtubeclone.data.responses.SubscribedChannels
import com.example.youtubeclone.services.FirebaseService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ActivityScoped
class FirebaseRespository @Inject constructor(
    private val db: FirebaseFirestore,
    private val currentuser: FirebaseUser?
) {
    suspend fun getChannelList(): List<SubscribedChannels> =
        suspendCoroutine { continuation ->
            val people = mutableListOf<SubscribedChannels>()
            val userDocRef = db.collection("subscribers").document(currentuser!!.uid)
                .collection(currentuser!!.uid)
            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val person = document.toObject<SubscribedChannels>()
                        if (person != null) {
                            people.add(person)
                        }
                    }
                    continuation.resume(people)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Unknown exception")
                    )
                }
            }
        }

    suspend fun addChannelList(item: SubscribedChannels): CompletableFuture<Boolean> {
        val userDocRef =
            db.collection("subscribers").document(currentuser!!.uid).collection(currentuser!!.uid)
        val future = CompletableFuture<Boolean>()
        userDocRef.document(item.videoid).set(item)

            .addOnSuccessListener {
                future.complete(true)
            }
            .addOnFailureListener {
                future.complete(false)
            }
        return future

    }

    suspend fun removeChannelList(item: SubscribedChannels): CompletableFuture<Boolean> {
        val userDocRef =
            db.collection("subscribers").document(currentuser!!.uid).collection(currentuser!!.uid)
                .document(item.videoid)
        val future = CompletableFuture<Boolean>()
        userDocRef.delete()
            .addOnSuccessListener {
                future.complete(true)
            }
            .addOnFailureListener {
                future.complete(false)
            }
        return future
    }

    suspend fun IschannelSubscribed(channelid: String): Boolean {
        val docRef =
            db.collection("subscribers").document(currentuser!!.uid).collection(currentuser!!.uid)
                .document(channelid)

        return try {
            val documentSnapshot = docRef.get().await()
            if (documentSnapshot.exists()) {
                true
            } else {
                false
            }
        } catch (exception: Exception) {

            Log.d("why there is exception", "IschannelSubscribed: " + exception.message!!)
            return false
            throw exception // Error occurred while fetching document
        }

    }

    suspend fun inserthistory(item: HistoryVideo): CompletableFuture<Boolean> {

        val userDocRef =
            db.collection("history").document(currentuser!!.uid).collection(currentuser!!.uid)
        val future = CompletableFuture<Boolean>()
        userDocRef.document(item.videoid).set(item)

            .addOnSuccessListener {
                future.complete(true)
            }
            .addOnFailureListener {
                future.complete(false)
            }
        return future

    }

    suspend fun getallHistory(): List<HistoryVideo> =
        suspendCoroutine { continuation ->
            val people = mutableListOf<HistoryVideo>()
            val userDocRef =
                db.collection("history").document(currentuser!!.uid).collection(currentuser!!.uid)
            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val person = document.toObject<HistoryVideo>()
                        if (person != null) {
                            people.add(person)
                        }
                    }
                    continuation.resume(people)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Unknown exception")
                    )
                }
            }
        }

    suspend fun getAllSavedVideo(): List<HistoryVideo> =
        suspendCoroutine { continuation ->
            val people = mutableListOf<HistoryVideo>()
            val userDocRef =
                db.collection("savedvideo").document(currentuser!!.uid).collection(currentuser!!.uid)
            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val person = document.toObject<HistoryVideo>()
                        if (person != null) {
                            people.add(person)
                        }
                    }
                    continuation.resume(people)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: Exception("Unknown exception")
                    )
                }
            }
        }

    suspend fun insertsavedvideo(item: HistoryVideo): CompletableFuture<Boolean> {

        val userDocRef =
            db.collection("savedvideo").document(currentuser!!.uid).collection(currentuser!!.uid)
        val future = CompletableFuture<Boolean>()
        userDocRef.document(item.videoid).set(item)

            .addOnSuccessListener {
                future.complete(true)
            }
            .addOnFailureListener {
                future.complete(false)
            }
        return future

    }


    suspend fun removesavedvideo(item: HistoryVideo): CompletableFuture<Boolean> {
        val userDocRef =
            db.collection("savedvideo").document(currentuser!!.uid).collection(currentuser!!.uid).document(item.videoid)
        val future = CompletableFuture<Boolean>()
        userDocRef.delete()
            .addOnSuccessListener {
                future.complete(true)
            }
            .addOnFailureListener {
                future.complete(false)
            }
        return future
    }


}
