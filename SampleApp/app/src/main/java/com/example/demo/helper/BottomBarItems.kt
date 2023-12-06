package com.example.demo.helper

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.jio.sdksampleapp.R

data class BottomBarItems(
    val imageName: String,
    val imageEnable: Int,
    val imageDisable: Int,
    var isSelected: Boolean,
    val borderColor: Color,
    var backgroundColor: Color
)

val transparent = Color(0x10f1f1f1)

val bottomControlList = mutableStateListOf(

    BottomBarItems(
        BottomItems.Audio.name,
        R.drawable.ic_mic_on,
        R.drawable.ic_mic_off,
        isSelected = true,
        borderColor = transparent,
        backgroundColor = transparent
    ),
    BottomBarItems(
        BottomItems.Video.name,
        R.drawable.ic_video_on,
        R.drawable.ic_video_off,
        isSelected = true,
        borderColor = transparent,
        backgroundColor = transparent
    ),
    BottomBarItems(
        BottomItems.Leave.name,
        R.drawable.ic_leave_call,
        R.drawable.ic_leave_call,
        isSelected = true,
        borderColor = transparent,
        backgroundColor = transparent
    ),
    BottomBarItems(
        BottomItems.ScreenShare.name,
        R.drawable.ic_screen_share,
        R.drawable.ic_stop_share,
        isSelected = true,
        borderColor = transparent,
        backgroundColor = transparent
    ),
)
enum class BottomItems {
    Video,
    Audio,
    Leave,
    ScreenShare
}

