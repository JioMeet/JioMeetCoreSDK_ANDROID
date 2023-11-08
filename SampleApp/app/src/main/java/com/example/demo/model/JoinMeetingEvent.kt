package com.example.demo.model

sealed interface JoinMeetingEvent
data class SetMeetingID(val meetingId: String) : JoinMeetingEvent
data class SetMeetingPin(val meetingPin: String) : JoinMeetingEvent
data class SetUserName(val userName: String) : JoinMeetingEvent
data class MeetingIDError(val isMeetingIDError: Boolean) : JoinMeetingEvent
data class MeetingPinError(val isMeetingPinError: Boolean) : JoinMeetingEvent
data class UserNameError(val isUserNameError: Boolean) : JoinMeetingEvent
data class SetJoinButtonEnabled(val isButtonEnabled: Boolean) : JoinMeetingEvent