package com.nhatvm.toptop.data.file

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.*
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface UploadProgressCallback {
    fun onProgress(progress: Int)
    fun onSuccess(downloadUrl: String)
    fun onFailure(exception: Exception)
}

fun openGalleryForVideo(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "video/*"
    launcher.launch(intent)
}
fun handleVideoUri(uri: Uri?, callback: UploadProgressCallback) {
    if (uri == null) {
        callback.onFailure(Exception("Uri is null"))
        return
    }

    val name = UUID.randomUUID().toString()
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val videoRef = storageRef.child("video/$name.mp4")

    val uploadTask = videoRef.putFile(uri)

    uploadTask.addOnProgressListener { taskSnapshot ->
        val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
        callback.onProgress(progress)
    }.addOnSuccessListener {
        videoRef.downloadUrl.addOnSuccessListener { uri ->
            callback.onSuccess(uri.toString())
        }.addOnFailureListener { e ->
            callback.onFailure(e)
        }
    }.addOnFailureListener { e ->
        callback.onFailure(e)
    }
}

fun createNotificationChannel(context: Context) {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
        val name = "UploadChannel"
        val descriptionText = "Channel for upload progress"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel("UPLOAD_CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

suspend fun handleVideoUri1(uri: Uri?): String? {
    val name = UUID.randomUUID().toString()
    return try {
        uri?.let {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val videoRef = storageRef.child("video/${name}.mp4")
            videoRef.putFile(uri).await()
            videoRef.downloadUrl.await().toString()
        }
    } catch (e: Exception) {
        null
    }
}

