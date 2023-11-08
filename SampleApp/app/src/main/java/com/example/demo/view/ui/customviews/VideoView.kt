package com.example.demo.view.ui.customviews

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.jiomeet.core.mediaEngine.agora.model.RenderView

@Composable
fun VideoView(videoView: RenderView) {
    AndroidView(
        factory = {
            FrameLayout(it)
        },
        update = { parentLayout ->
            if (videoView.view.parent == null) {
                if (parentLayout.childCount > 0) {
                    parentLayout.removeAllViews()
                    parentLayout.invalidate()
                }
                parentLayout.addView(videoView.view)
            } else {
                (videoView.view.parent as? ViewGroup)?.apply {
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