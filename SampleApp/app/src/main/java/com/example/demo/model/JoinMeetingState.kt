package com.example.demo.model

data class JoinMeetingState(
    val meetingID: String = "",
    val meetingPin: String = "",
    val userName: String = "",
    val isMeetingIDError: Boolean = false,
    val isMeetingPinError: Boolean = false,
    val isUserNameError: Boolean = false,
    val isJoinButtonEnabled: Boolean = false
)
