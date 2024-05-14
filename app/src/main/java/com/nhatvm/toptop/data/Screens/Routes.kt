package com.nhatvm.toptop.data.Screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Routes {
    const val HOME_SCREEN ="HOME_SCREEN"
    const val USER_SCREEN ="USER_SCREEN"
    const val LOGIN_SCREEN ="LOGIN_SCREEN"
    var OPTION_SCREEN by mutableStateOf(false)
        public set

    operator fun getValue(thisRef: Any?, property: Any?): Boolean {
        return OPTION_SCREEN
    }

    operator fun setValue(thisRef: Any?, property: Any?, value: Boolean) {
        OPTION_SCREEN = value
    }
}

