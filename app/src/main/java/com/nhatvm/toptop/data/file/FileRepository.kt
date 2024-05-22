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

    fun DownloadVideoFromFirebaseStorage(context: Context, storagePath:String, url:String) {
        val storagePath = storagePath // Đường dẫn của video trên Firebase Storage
        val localFile = File(context.filesDir, url) // Đường dẫn của tệp cục bộ để lưu trữ video

        GlobalScope.launch(Dispatchers.IO) {
            val success = prepareDownloadVideoFromFirebaseStorage(storagePath, localFile)
            if (success) {

            } else {
                // Xử lý khi có lỗi xảy ra trong quá trình tải xuống video
            }
        }
    }

}
suspend fun prepareDownloadVideoFromFirebaseStorage(storagePath: String, localFile: File): Boolean {
    return try {
        // Lấy reference của Firebase Storage
        val storageReference = Firebase.storage.reference.child(storagePath)

        // Tạo tệp tạm để lưu video tải về
        val tempFile = File.createTempFile("temp_video", ".mp4")

        // Tải dữ liệu từ Firebase Storage
        storageReference.getFile(Uri.fromFile(tempFile)).await()

        // Sao chép dữ liệu từ tệp tạm sang tệp cục bộ
        tempFile.copyTo(localFile, overwrite = true)

        // Xoá tệp tạm sau khi đã sao chép xong
        tempFile.delete()

        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

