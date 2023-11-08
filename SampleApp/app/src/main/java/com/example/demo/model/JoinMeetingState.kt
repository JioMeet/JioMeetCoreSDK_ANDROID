package com.example.demo.model

data class JoinMeetingState(
    val meetingID: String = "5165217773",
    val meetingPin: String = "yB7FN",
    val userName: String = "",
    val isMeetingIDError: Boolean = false,
    val isMeetingPinError: Boolean = false,
    val isUserNameError: Boolean = false,
    val isJoinButtonEnabled: Boolean = false
)

