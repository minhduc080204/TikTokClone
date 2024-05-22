package com.nhatvm.toptop.data.video.repository

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRepository @Inject constructor() {
    suspend fun getVideoUrls(): List<String> {
        val storage = FirebaseStorage.getInstance()
        val listRef: StorageReference = storage.reference.child("video")
        return try {
            val listResult = listRef.listAll().await()
            val urlList = listResult.items.map { itemRef ->
                itemRef.downloadUrl.await().toString()
            }
            urlList
        } catch (e: Exception) {
            println("Error getting list of videos: ${e.message}")
            emptyList()
        }
    }

    suspend fun getVideo() = getVideoUrls()

    suspend fun getVideoCount(): Int = getVideoUrls().size
}