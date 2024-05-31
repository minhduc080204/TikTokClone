package com.nhatvm.toptop.data.video.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nhatvm.toptop.data.auth.repositories.User
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
            emptyList()
        }
    }

    suspend fun getUserById(userId: String, onSuccess:() -> Unit): User{
        var user = User("@@", "ADMIN", "0359100", "@admin")
        val fireDatabase = FirebaseDatabase.getInstance()
        val userRef = fireDatabase.getReference("users").child(userId)
        val snapshot = userRef.get().await()
        if (snapshot.exists()) {
            val name = snapshot.child("name").getValue(String::class.java)
            val phone = snapshot.child("phone").getValue(String::class.java)
            val username = snapshot.child("username").getValue(String::class.java)
            user = User(userId, name?:"", phone?:"", username?:"")
            onSuccess()
        }
        return user
    }
    suspend fun getVideoObject(): MutableList<Video> {
    val videos = mutableListOf<Video>()
    val fireDatabase = FirebaseDatabase.getInstance()

    val videosRef = fireDatabase.getReference("videos")
    val snapshot = videosRef.get().await()
    if (snapshot.exists()) {
        for (videoSnapshot in snapshot.children) {
            val urlVideo = videoSnapshot.child("urlvideo").getValue(String::class.java) ?: "ddd"
            val usernameVideo = videoSnapshot.child("usernamevideo").getValue(String::class.java) ?: "ddd"
            val contentVideo = videoSnapshot.child("contentVideo").getValue(String::class.java) ?: "ddd"
            val songVideo = videoSnapshot.child("songVideo").getValue(String::class.java) ?: "ddd"
            val likeVideo = videoSnapshot.child("likeVideo").getValue(Int::class.java) ?: 12
            val shareVideo = videoSnapshot.child("shareVideo").getValue(Int::class.java) ?: 12
            val tags = videoSnapshot.child("tagsVideo").children.map { it.getValue(String::class.java) ?: "" }

            val commentsRef = videoSnapshot.child("commentVideo")
            val commentsObject = mutableListOf<Comment>()

            for (commentRef in commentsRef.children) {
                val comment = commentRef.child("comment").getValue(String::class.java) ?: ""
                val likeComment = commentRef.child("likeComment").getValue(Int::class.java) ?: 0
                val timeComment = commentRef.child("timeComment").getValue(String::class.java) ?: ""
                val userIdComment = commentRef.child("userId").getValue(String::class.java) ?: ""

                val userComment = getUserById(userIdComment, {})
                commentsObject.add(
                    Comment(
                        userComment,
                        comment,
                        likeComment,
                        timeComment
                    )
                )
            }

            val userVideo = getUserById(videoSnapshot.child("userId").getValue(String::class.java)?:"", {})
            val video = Video(
                userVideo,
                urlVideo,
                usernameVideo,
                contentVideo,
                tags,
                songVideo,
                commentsObject,
                likeVideo,
                shareVideo
            )
            videos.add(video)
        }
    }
    return videos
}
    suspend fun getCommentsByVideoId(videoId: String): MutableList<Comment> {
        val comments = mutableListOf<Comment>()
        val fireDatabase = FirebaseDatabase.getInstance()

        val commentsRef = fireDatabase.getReference("videos").child(videoId).child("commentVideo")
        val snapshot = commentsRef.get().await()
        if (snapshot.exists()) {
            for (commentSnapshot in snapshot.children) {
                val comment = commentSnapshot.child("comment").getValue(String::class.java) ?: ""
                val likeComment = commentSnapshot.child("likeComment").getValue(Int::class.java) ?: 0
                val timeComment = commentSnapshot.child("timeComment").getValue(String::class.java) ?: ""
                val userIdComment = commentSnapshot.key ?: ""

                val userComment = getUserById(userIdComment, {})

                comments.add(
                    Comment(
                        userComment = userComment,
                        comment = comment,
                        likeComment = likeComment,
                        timeComment = timeComment
                    )
                )
            }
        }
        return comments
    }

    suspend fun getVideoCount(): Int = getVideoUrls().size
}
class Video(
    val userVideo: User,
    val urlVideo: String,
    val usernameVideo: String,
    val contentVideo: String,
    val tagsVideo: List<String>,
    val songVideo: String,
    val commentVideo: List<Comment>,
    val likeVideo: Int,
    val shareVideo: Int,
)

class Comment(
    val userComment: User,
    val comment: String,
    val likeComment: Int,
    val timeComment: String,
)