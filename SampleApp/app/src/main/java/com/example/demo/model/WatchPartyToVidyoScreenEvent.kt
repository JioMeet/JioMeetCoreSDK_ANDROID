package com.example.demo.model

sealed interface WatchPartyToVidyoScreenEvent {

    object OnCamControlChangeEvent: WatchPartyToVidyoScreenEvent

    object OnMicControlChangeEvent: WatchPartyToVidyoScreenEvent

    object InviteParticipantEvent: WatchPartyToVidyoScreenEvent

    object CloseMeetingEvent: WatchPartyToVidyoScreenEvent

    data class ChangePartyVolumeEvent(val progressVolume: Int) : WatchPartyToVidyoScreenEvent

    object InitMediaHelper : WatchPartyToVidyoScreenEvent

    data class OnAudioOnlyModeEvent(val isAudioOnlyEnabled: Boolean): WatchPartyToVidyoScreenEvent

    object OnChatChangeEvent:WatchPartyToVidyoScreenEvent
    object OnScreenShareStartEvent : WatchPartyToVidyoScreenEvent
    object OnScreenShareStopEvent : WatchPartyToVidyoScreenEvent
}