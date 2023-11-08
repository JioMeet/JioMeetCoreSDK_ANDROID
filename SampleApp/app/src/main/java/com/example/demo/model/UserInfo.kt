package com.example.demo.model

import com.jiomeet.core.constant.Constant
import com.jiomeet.core.mediaEngine.agora.model.RenderView
import java.util.Locale

data class UserInfo(
    val uid: String,
    val userId: String,
    var name: String = "",
    val videoView: RenderView?= null,
    val isLocalUser:Boolean = false,
    var isAudioMuted: Boolean = false,
    var isVideoMuted: Boolean = false,
    var isHandRaised: Boolean = false,
    var isActiveSpeaker: Boolean = false,
    var isScreenShared: Boolean = false,
    var isHost: Boolean = false,
    var isCoHost: Boolean = false,
    var width: Float = 0f,
    var height: Float = 0f,
    var bottomPadding: Float = 0f,
    var endPadding: Float = 0f,
    var startPadding:Float =0f,
    var participantType:String = Constant.ParticipantType.TYPE_SPEAKER
) {
    fun displayName(): String {
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + if (isLocalUser) " (You)" else ""
    }
}