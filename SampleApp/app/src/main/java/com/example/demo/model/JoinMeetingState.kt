package com.example.demo.model

data class JoinMeetingState(
    val meetingID: String = "8748702229",
    val meetingPin: String = "c8KrZ",
    val userName: String = "",
    val isMeetingIDError: Boolean = false,
    val isMeetingPinError: Boolean = false,
    val isUserNameError: Boolean = false,
    val isJoinButtonEnabled: Boolean = false
)
