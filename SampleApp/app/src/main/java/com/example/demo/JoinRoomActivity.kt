package com.example.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.demo.helper.StringConstants
import com.example.demo.model.JioMeetConnectionListener
import com.example.demo.view.ui.LaunchCore
import com.jiomeet.core.constant.Constant
import com.jiomeet.core.main.models.JMJoinMeetingConfig
import com.jiomeet.core.main.models.JMJoinMeetingData
import com.jiomeet.core.main.models.Speaker
import com.jiomeet.core.utils.BaseUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinRoomActivity : ComponentActivity() {

    companion object {
        const val TAG = "RoomActivity"
    }

    private val jmJoinMeetingConfig =
            JMJoinMeetingConfig(
                    userRole = Speaker,
                    isInitialAudioOn = true,
                    isInitialVideoOn = true,
                    isShareScreen = false,
                    isShareWhiteBoard = false
            )

    private val jioMeetConnectionListener =
            object : JioMeetConnectionListener {
                override fun onLeaveMeeting() {
                    finish()
                }
            }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUrl.initializedNetworkInformation(this@JoinRoomActivity, Constant.Environment.PROD)
        joinVideoCall()
    }

    private fun joinVideoCall() {
        setContent {
            val jmJoinMeetingData =
                    JMJoinMeetingData(
                            meetingId = intent.getStringExtra(StringConstants.MEETING_ID) ?: "",
                            meetingPin = intent.getStringExtra(StringConstants.MEETING_PIN) ?: "",
                            displayName = intent.getStringExtra(StringConstants.GUEST_NAME) ?: "",
                            version = "1234",
                            deviceId = "deviceId"
                    )
            LaunchCore(
                    jioMeetConnectionListener = jioMeetConnectionListener,
                    jmJoinMeetingConfig = jmJoinMeetingConfig,
                    jmJoinMeetingData = jmJoinMeetingData
            )
        }
    }
}
