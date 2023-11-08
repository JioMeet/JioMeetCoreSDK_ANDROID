package com.example.demo.view.ui

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.demo.model.CoreData
import com.example.demo.viewModel.JMClientViewModel
import com.jio.sdksampleapp.R
import com.jiomeet.core.network.api.participants.model.RoomDetailsWithPinState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun InitiateCore(
    coreData: CoreData, jmClientViewModel: JMClientViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        loadMeetingDetails(this, context,jmClientViewModel)
        jmClientViewModel.joinMeeting()
    }
    jmClientViewModel.jmJoinMeetingData = coreData.jmJoinMeetingData
    jmClientViewModel.jmJoinMeetingConfig = coreData.jmJoinMeetingConfig
    jmClientViewModel.jioMeetConnectionListener = coreData.coreListener
    BackHandler(onBack = {
        coreData.coreListener.onLeaveMeeting()
    })

    MainScreen(viewModel = jmClientViewModel)
}

private fun loadMeetingDetails(scope: CoroutineScope, context: Context, jmClientViewModel: JMClientViewModel) {
    scope.launch {
        jmClientViewModel.getRoomDetails().collect {
            when (it) {
                is RoomDetailsWithPinState.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.text_enter_correct_meeting_details),
                        Toast.LENGTH_SHORT
                    ).show()

                }

                is RoomDetailsWithPinState.Loading -> {

                }

                is RoomDetailsWithPinState.Success -> {
                    joinMeeting(jmClientViewModel)
                }

                RoomDetailsWithPinState.MeetingEnded -> {

                }

                RoomDetailsWithPinState.MeetingFull -> {

                }
            }
        }
    }
}

private fun joinMeeting(jmClientViewModel: JMClientViewModel) {
    jmClientViewModel.initAudioWrapper()
    jmClientViewModel.joinRoom()
}
