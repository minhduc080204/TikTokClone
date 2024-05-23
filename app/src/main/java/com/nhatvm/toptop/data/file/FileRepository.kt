package com.nhatvm.toptop.data.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class FileRepository {

    fun openGalleryForVideo(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        launcher.launch(intent)
    }
    fun handleVideoUri(uri: Uri?) {
        uri?.let {
            // Do something with the video URI, e.g., play the video or save the URI for later use
        }
    }
}