package com.example.demo.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.helper.BottomBarItems
import com.example.demo.helper.StateEventWithContent
import com.example.demo.helper.bottomControlList
import com.example.demo.helper.consumed
import com.example.demo.helper.triggered
import com.example.demo.model.JioMeetConnectionListener
import com.example.demo.model.UserInfo
import com.example.demo.model.WatchPartyToVidyoScreenEvent
import com.example.demo.service.OnGoingScreenShareService
import com.jio.sdksampleapp.R
import com.jiomeet.core.CoreApplication
import com.jiomeet.core.log.Logger
import com.jiomeet.core.main.JMClient
import com.jiomeet.core.main.event.LocalScreenShareStart
import com.jiomeet.core.main.event.LocalScreenShareStop
import com.jiomeet.core.main.event.OnRemoteUserJoinMeeting
import com.jiomeet.core.main.event.OnRemoteUserLeftMeeting
import com.jiomeet.core.main.event.OnRemoteUserMicStatusChanged
import com.jiomeet.core.main.event.OnRemoteUserVideoStatusChanged
import com.jiomeet.core.main.event.OnScreenShareStarted
import com.jiomeet.core.main.event.OnScreenShareStop
import com.jiomeet.core.main.event.OnUserJoinMeeting
import com.jiomeet.core.main.event.OnUserLeaveMeeting
import com.jiomeet.core.main.event.OnUserMicStatusChanged
import com.jiomeet.core.main.event.OnUserVideoStatusChanged
import com.jiomeet.core.main.models.JMJoinMeetingConfig
import com.jiomeet.core.main.models.JMJoinMeetingData
import com.jiomeet.core.main.models.JMMeetingUser
import com.jiomeet.core.network.api.participants.model.RoomDetailsWithPinState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Import the necessary classes and packages

// 1. Create an instance of JMClientViewModel by injecting it with JMClient as a dependency.

// Example of injecting JMClientViewModel using Hilt:
@HiltViewModel
class JMClientViewModel @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) : ViewModel() {

    private val jmClient = CoreApplication.coreMainModule.jmClient

    init {
        jmClient.init()
        collectJmClientCoHostEvent()
        // Perform other setup operations here if needed
    }

    var jmJoinMeetingData: JMJoinMeetingData? = null
    var jmJoinMeetingConfig: JMJoinMeetingConfig? = null
    var jioMeetConnectionListener: JioMeetConnectionListener? = null

    private val _isProgressDialogStateFlow = MutableStateFlow(false)
    val isProgressDialogFlow = _isProgressDialogStateFlow.asStateFlow()

    private val _remoteUsers = MutableStateFlow<List<UserInfo>>(emptyList())
    val remoteUsers: StateFlow<List<UserInfo>> = _remoteUsers.asStateFlow()

    private val _screenShareInProgress = MutableStateFlow(false)
    val screenShareInProgress = _screenShareInProgress.asStateFlow()

    private val _screenShareUser = MutableStateFlow<List<UserInfo>>(emptyList())
    val screenShareUser: StateFlow<List<UserInfo>> = _screenShareUser.asStateFlow()

    private val _localUser = MutableStateFlow<UserInfo?>(null)
    val localUser: StateFlow<UserInfo?> = _localUser.asStateFlow()

    private val _micStatus = MutableStateFlow(jmJoinMeetingConfig?.isInitialAudioOn == true)
    val micStatus: StateFlow<Boolean> = _micStatus.asStateFlow()

    private val _camStatus = MutableStateFlow(jmJoinMeetingConfig?.isInitialVideoOn == true)
    val camStatus: StateFlow<Boolean> = _camStatus.asStateFlow()

    private val _isErrorState = MutableStateFlow(false)
    val isErrorState = _isErrorState.asStateFlow()

    private val _bottomBarImages = MutableStateFlow(bottomControlList)
    val bottomImageItems: StateFlow<MutableList<BottomBarItems>> = _bottomBarImages.asStateFlow()

    private val roomDetailsFromPin =
        MutableStateFlow<RoomDetailsWithPinState>(RoomDetailsWithPinState.Loading(true))


    private val _watchPartyToVidyoScreenEvent =
        mutableStateOf<StateEventWithContent<WatchPartyToVidyoScreenEvent>>(consumed())


    companion object {
        const val TAG = "WatchPartyActivity"
    }

    private fun collectJmClientCoHostEvent() {
        viewModelScope.launch {
            jmClient.observeJmClientEvent().collect {
                when (it) {
                    is OnUserJoinMeeting -> {
                        setLocalUser(it.user)
                    }

                    is OnRemoteUserJoinMeeting -> {
                        appendRemoteUser(it.user)
                    }

                    is OnUserLeaveMeeting -> {
                        jioMeetConnectionListener?.onLeaveMeeting()
                    }

                    is OnRemoteUserLeftMeeting -> {
                        removeRemoteUser(it.user.userId)

                    }

                    is OnUserMicStatusChanged -> {
                        var currentLocalUser = _localUser.value
                        currentLocalUser = currentLocalUser?.copy(isAudioMuted = it.isMuted)
                        if (currentLocalUser != null) {
                            _localUser.emit(currentLocalUser)
                        }

                    }

                    is OnUserVideoStatusChanged -> {
                        var currentLocalUser = _localUser.value
                        currentLocalUser = currentLocalUser?.copy(isVideoMuted = it.isMuted)
                        if (currentLocalUser != null) {
                            _localUser.emit(currentLocalUser)
                        }
                    }

                    is OnRemoteUserMicStatusChanged -> {
                        val currentRemoteUsers = _remoteUsers.value
                        val idx = currentRemoteUsers.indexOfFirst { user ->
                            user.userId == it.user.userId
                        }
                        if (idx > 0) {
                            currentRemoteUsers[idx].copy(isAudioMuted = it.isMuted)
                        }
                        _remoteUsers.emit(currentRemoteUsers)
                    }

                    is OnRemoteUserVideoStatusChanged -> {
                        val currentRemoteUsers = _remoteUsers.value.toMutableList()
                        val idx = currentRemoteUsers.indexOfFirst { user ->
                            user.userId == it.user.userId
                        }
                        if (idx >= 0) {
                            currentRemoteUsers[idx] =
                                currentRemoteUsers[idx].copy(isVideoMuted = it.isMuted)
                            _remoteUsers.emit(currentRemoteUsers)
                        }
                    }

                    is LocalScreenShareStart -> {
                        _screenShareInProgress.value = true
                    }

                    is LocalScreenShareStop -> {
                        stopScreenShareNotification(applicationContext)
                        _screenShareInProgress.value = false
                    }

                    is OnScreenShareStarted -> {


                    }

                    is OnScreenShareStop -> {


                    }


                    else -> {}
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    fun joinMeeting() {
        viewModelScope.launch(Dispatchers.IO) {
            jmJoinMeetingData?.let {
                jmJoinMeetingConfig?.let { it1 ->
                    jmClient.joinMeeting(it, it1) {
                        it?.let {
                            roomDetailsFromPin.value = RoomDetailsWithPinState.Success(it)
                            setIsProgressDialog(true)
                            setIsErrorState(true)
                        }
                    }
                }
            }
        }
    }

    private fun setIsProgressDialog(isProgressDialog: Boolean) {
        viewModelScope.launch {
            _isProgressDialogStateFlow.emit(isProgressDialog)
        }
    }

    private fun setIsErrorState(iError: Boolean) {
        viewModelScope.launch {
            _isErrorState.emit(iError)
        }
    }

    fun getRoomDetails(): StateFlow<RoomDetailsWithPinState> {
        return roomDetailsFromPin
    }

    fun initAudioWrapper() {
        jmClient.initializeAudioWrapper()
    }

    fun joinRoom() {
        jmClient.joinRoom()
    }

    private fun appendRemoteUser(remoteUser: JMMeetingUser) {
        val user = remoteUser.rendererView?.let {
            UserInfo(
                uid = remoteUser.uid,
                videoView = it,
                name = remoteUser.displayName ?: "",
                isAudioMuted = remoteUser.isAudioMuted,
                isVideoMuted = remoteUser.isVideoMuted,
                isHandRaised = remoteUser.isHandRaised,
                isHost = remoteUser.isHost,
                isCoHost = remoteUser.isCoHost,
                isLocalUser = true,
                userId = remoteUser.userId
            )
        }

        viewModelScope.launch {
            val currentRemoteUsers = _remoteUsers.value.toMutableList()
            if (user != null) {
                currentRemoteUsers.add(user)
            }
            _remoteUsers.emit(currentRemoteUsers)
        }
    }

    // Function to remove a remote user from the list (if needed)
    private fun removeRemoteUser(remoteUserId: String) {
        viewModelScope.launch {
            val currentRemoteUsers = _remoteUsers.value.toMutableList()
            currentRemoteUsers.removeAll { remoteUser ->
                remoteUser.userId == remoteUserId
            }
            _remoteUsers.emit(currentRemoteUsers)
        }
    }

    private fun setLocalUser(user: JMMeetingUser) {
        val localUser = user.rendererView?.let { renderView ->
            UserInfo(
                uid = user.uid,
                videoView = renderView,
                name = user.displayName ?: "",
                isAudioMuted = user.isAudioMuted,
                isVideoMuted = user.isVideoMuted,
                isHandRaised = user.isHandRaised,
                isHost = user.isHost,
                isCoHost = user.isCoHost,
                isLocalUser = true,
                userId = user.userId
            )
        }
        viewModelScope.launch {
            _localUser.emit(localUser)
            _bottomBarImages.value[0].isSelected = jmJoinMeetingConfig?.isInitialAudioOn == true
            _bottomBarImages.value[1].isSelected = jmJoinMeetingConfig?.isInitialVideoOn == true
        }
        toggleCamera(user.isVideoMuted)
        toggleMic(user.isAudioMuted)
    }

    fun toggleMic(toMute: Boolean) {
        viewModelScope.launch {
            jmClient.muteLocalAudio(toMute)
            _micStatus.value = toMute
            _bottomBarImages.value[0] = _bottomBarImages.value[0].copy(isSelected = !toMute)
        }

    }

    fun toggleCamera(toMute: Boolean) {
        viewModelScope.launch {
            jmClient.muteLocalVideo(toMute)
            _camStatus.value = toMute
            _bottomBarImages.value[1] = _bottomBarImages.value[1].copy(isSelected = !toMute)
        }
    }

    fun leaveCall() {
        viewModelScope.launch {
            jmClient.leaveMeeting()
            jioMeetConnectionListener?.onLeaveMeeting()
        }
    }

    fun toggleScreenShare(toMute: Boolean) {
        viewModelScope.launch {
            jmClient.screenShareState(!toMute)
            _bottomBarImages.value[3] = _bottomBarImages.value[3].copy(isSelected = toMute)
        }
    }

    fun showScreenShareNotification(context: Context) {
        try {
            val notificationServiceIntent =
                Intent(context, OnGoingScreenShareService::class.java)
            notificationServiceIntent.putExtra(
                "notification_text",
                "Screen Share in Progress"
            )
            ContextCompat.startForegroundService(context, notificationServiceIntent)
        } catch (e: Throwable) {
            Logger.error(TAG, " showScreenShareNotification Failed${e.message}")
        }
    }

    fun stopScreenShareNotification(context: Context) {
        try {
            val notificationServiceIntent =
                Intent(context, OnGoingScreenShareService::class.java)
            context.stopService(notificationServiceIntent)
        } catch (e: Throwable) {
            Logger.error(TAG, "stopScreenShareNotification Failed${e.message}")
        }
    }

}