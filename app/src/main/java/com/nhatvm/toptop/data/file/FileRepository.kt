package com.nhatvm.toptop.data.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

class FileRepository {

    fun openGalleryForVideo(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        launcher.launch(intent)
    }
    suspend fun handleVideoUri(uri: Uri?): String? {
        val name = UUID.randomUUID().toString()
        return try {
            uri?.let {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val videoRef = storageRef.child("videos/${name}.mp4")
                videoRef.putFile(uri).await()
                videoRef.downloadUrl.await().toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun uploadVideoToFirebase(videoUri: Uri){
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val videoRef = storageRef.child("videos/${UUID.randomUUID()}.mp4")
        videoRef.putFile(videoUri).await()
    }
}