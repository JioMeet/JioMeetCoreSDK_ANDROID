package com.example.demo.helper

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.demo.model.UserInfo
import com.jiomeet.core.mediaEngine.agora.model.RenderView

@Composable
fun UserView(
    user: UserInfo,
    isLocalUser: Boolean = false,
    height: Dp,
    width: Dp
) {

    val tileBackground = Color(0xFF2B2B2B)
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(tileBackground)
    ) {

        if (user.isVideoMuted) {
            ShortNameView(
                modifier = Modifier.align(Alignment.Center),
                shortName = HelperUtility.getFirstLastCharName(user.name)
            )
        } else {
            user.videoView?.let { VideoView(videoView = it) }
        }

        FirstNameView(
            modifier = Modifier.align(Alignment.BottomStart),
            if (isLocalUser)
                "You"
            else
                user.name.substringBefore(' '),  user.isAudioMuted
        )
    }
}

@Composable
private fun ShortNameView(modifier: Modifier, shortName: String) {
    Text(
        text = shortName,
        fontSize = 24.sp,
        color = Color.White,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}

@Composable
fun VideoView(videoView: RenderView) {
    AndroidView(
        factory = {
            FrameLayout(it)
        },
        update = { parentLayout ->
            if (videoView.view?.parent == null) {
                if (parentLayout.childCount > 0) {
                    parentLayout.removeAllViews()
                    parentLayout.invalidate()
                }
                parentLayout.addView(videoView.view)
            } else {
                (videoView.view?.parent as? ViewGroup)?.apply {
                    if (this != parentLayout) {
                        parentLayout.removeAllViews()
                        parentLayout.invalidate()
                        this.removeView(videoView.view)
                        this.invalidate()
                        parentLayout.addView(videoView.view)
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

//XML Equivalent of above function to set the videoview in a framelayout
//<!-- res/layout/video_view_layout.xml -->
//<FrameLayout
//xmlns:android="http://schemas.android.com/apk/res/android"
//android:id="@+id/videoContainer"
//android:layout_width="match_parent"
//android:layout_height="match_parent">
//</FrameLayout>

// Inflate the XML layout that contains a FrameLayout with an ID of 'videoContainer'
//val view = inflater.inflate(R.layout.video_view_layout, container, false)

// Find the FrameLayout with the ID 'videoContainer' from the inflated view
//videoContainer = view.findViewById(R.id.videoContainer)


// Remove any existing views within the FrameLayout and refresh the layout
//if (videoContainer != null) {
//    if (videoContainer?.childCount ?: 0 > 0) {
//        videoContainer?.removeAllViews()
//        videoContainer?.invalidate()
//    }

// Add the video view (or any other view) to the FrameLayout
//    videoContainer?.addView(videoView.view)
//}



@Composable
private fun FirstNameView(modifier: Modifier, text: String,  isAudioMuted: Boolean) {
    Row(modifier = modifier.padding(start = 10.dp, bottom = 4.dp)) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(start = 10.dp)
        )
    }
}