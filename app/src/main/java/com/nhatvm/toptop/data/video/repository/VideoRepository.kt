package com.nhatvm.toptop.data.video.repository

import com.nhatvm.toptop.data.R
import javax.inject.Inject

class VideoRepository @Inject constructor() {

    private val videos = listOf(
        R.raw.choingu1,
        R.raw.choingu2,
        R.raw.choingu3,
        R.raw.choingu4,
    )

    fun getVideo() = videos.random()

}