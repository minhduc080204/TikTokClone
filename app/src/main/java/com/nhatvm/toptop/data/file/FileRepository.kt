package com.nhatvm.toptop.data.file

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

fun openGalleryForVideo(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "video/*"
    launcher.launch(intent)
}
suspend fun uploadVideotoTopTop(uri: Uri?): String? {
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

fun downloadVideo(context: Context, url: String, fileName: String) {
    Toast.makeText(context, "Đang tải video xuống...", Toast.LENGTH_LONG).show()
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(fileName)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, fileName)

    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    manager.enqueue(request)
    Toast.makeText(context, "Tải video thành công !", Toast.LENGTH_SHORT).show()
}