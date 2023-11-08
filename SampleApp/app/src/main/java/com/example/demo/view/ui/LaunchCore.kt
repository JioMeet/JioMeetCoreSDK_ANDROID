package com.example.demo.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.demo.model.CoreData
import com.example.demo.model.JioMeetConnectionListener
import com.example.demo.view.ui.customviews.ProgressDialog
import com.example.demo.viewModel.JMClientViewModel
import com.jiomeet.core.main.models.JMJoinMeetingConfig
import com.jiomeet.core.main.models.JMJoinMeetingData

@Composable
fun LaunchCore(
    jioMeetConnectionListener: JioMeetConnectionListener,
    jmJoinMeetingConfig: JMJoinMeetingConfig,
    jmJoinMeetingData: JMJoinMeetingData
) {

    if (jmJoinMeetingData.meetingId.isNotBlank() && jmJoinMeetingData.meetingPin.isNotBlank()) {
        val coreData = CoreData(
            clientToken = "",
            coreListener = jioMeetConnectionListener,
            hostToken = jmJoinMeetingData.hostToken,
            jmJoinMeetingConfig = jmJoinMeetingConfig,
            jmJoinMeetingData = jmJoinMeetingData
        )
        CoreScreen(coreData)
    }
}

@Composable
private fun CoreScreen(coreData: CoreData) {
    Column {
        CoreNav(coreData = coreData)
    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CoreNav(
    coreTemplateViewModel: JMClientViewModel = hiltViewModel(),
    coreData: CoreData
) {
    Scaffold {

        val isProgressDialogFlow by coreTemplateViewModel.isProgressDialogFlow.collectAsStateWithLifecycle()
        val isErrorState by coreTemplateViewModel.isErrorState.collectAsStateWithLifecycle()

        if (!isProgressDialogFlow) {
            ProgressDialog()
        } else if (!isErrorState) {
            coreData.coreListener.onLeaveMeeting()
        }
        InitiateCore(
            coreData,
            coreTemplateViewModel
        )
    }

}