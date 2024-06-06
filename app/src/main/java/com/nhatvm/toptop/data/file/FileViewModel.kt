package com.nhatvm.toptop.data.file

import android.app.Application
import android.app.NotificationManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nhatvm.toptop.data.R
import kotlinx.coroutines.launch
import javax.inject.Inject

class FileViewModel @Inject constructor(
    application: Application,
    private val notificationManager: NotificationManager
) : AndroidViewModel(application) {

    private val notificationBuilder = NotificationCompat.Builder(application, "UPLOAD_CHANNEL_ID")
        .setSmallIcon(R.drawable.loading_icon) // Đặt icon của bạn ở đây
        .setContentTitle("Uploading Video")
        .setPriority(NotificationCompat.PRIORITY_LOW)

    init {
        createNotificationChannel(application)
    }

    fun uploadVideo(uri: Uri) {
        viewModelScope.launch {
            handleVideoUri(uri, object : UploadProgressCallback {
                override fun onProgress(progress: Int) {
                    updateNotification(progress)
                }

                override fun onSuccess(downloadUrl: String) {
                    completeNotification(downloadUrl)
                }

                override fun onFailure(exception: Exception) {
                    // Handle the error appropriately in your app
                    notificationManager.cancel(1)
                }
            })
        }
    }

    private fun updateNotification(progress: Int) {
        notificationBuilder.setProgress(100, progress, false)
            .setContentText("$progress%")
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun completeNotification(downloadUrl: String) {
        notificationBuilder.setContentText("Upload complete")
            .setProgress(0, 0, false)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
