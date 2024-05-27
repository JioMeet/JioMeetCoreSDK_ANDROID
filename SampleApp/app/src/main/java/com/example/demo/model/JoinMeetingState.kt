package com.example.demo.model
//https://jiomeetpro.jio.com/shortener?meetingId=2555164860&pwd=bMF7c
data class JoinMeetingState(
    val meetingID: String = "2555164860",
    val meetingPin: String = "bMF7c",
    val userName: String = "",
    val isMeetingIDError: Boolean = false,
    val isMeetingPinError: Boolean = false,
    val isUserNameError: Boolean = false,
    val isJoinButtonEnabled: Boolean = false
)
