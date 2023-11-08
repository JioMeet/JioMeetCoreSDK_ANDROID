package com.example.demo.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.demo.model.JoinMeetingEvent
import com.example.demo.model.JoinMeetingState
import com.example.demo.model.MeetingIDError
import com.example.demo.model.MeetingPinError
import com.example.demo.model.SetJoinButtonEnabled
import com.example.demo.model.SetMeetingID
import com.example.demo.model.SetMeetingPin
import com.example.demo.model.SetUserName
import com.example.demo.model.UserNameError

class AppViewModel : ViewModel() {
    private val _loginState = mutableStateOf(JoinMeetingState())
    val loginState: State<JoinMeetingState> = _loginState
    fun onEvent(event: JoinMeetingEvent) {
        _loginState.value = when (event) {
            is SetMeetingID -> loginState.value.copy(meetingID = event.meetingId)
            is SetMeetingPin -> loginState.value.copy(meetingPin = event.meetingPin)
            is SetUserName -> loginState.value.copy(userName = event.userName)
            is MeetingIDError -> loginState.value.copy(isMeetingIDError = event.isMeetingIDError)
            is MeetingPinError -> loginState.value.copy(isMeetingPinError = event.isMeetingPinError)
            is UserNameError -> loginState.value.copy(isUserNameError = event.isUserNameError)
            is SetJoinButtonEnabled -> {
                if (loginState.value.meetingID.isEmpty()
                        .not() && loginState.value.meetingPin.isEmpty()
                        .not() && loginState.value.userName.isEmpty().not()
                ) {
                    loginState.value.copy(isJoinButtonEnabled = true)
                } else {
                    loginState.value.copy(isJoinButtonEnabled = false)
                }
            }
        }
    }

    fun isJoinButtonEnabled(): Boolean {
        if ((loginState.value.meetingID.isEmpty()) || loginState.value.meetingID.length != 10) {
            onEvent(MeetingIDError(true))
        }
        if ((loginState.value.meetingPin.isEmpty())) {
            onEvent(MeetingPinError(true))
        }
        if ((loginState.value.userName.isEmpty())) {
            onEvent(UserNameError(true))
        }
        return loginState.value.isMeetingIDError.not() && loginState.value.isMeetingPinError.not() && loginState.value.isUserNameError.not()
    }
}

