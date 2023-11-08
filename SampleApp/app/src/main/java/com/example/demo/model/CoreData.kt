package com.example.demo.model

import com.jiomeet.core.main.models.JMJoinMeetingConfig
import com.jiomeet.core.main.models.JMJoinMeetingData

data class CoreData(
    val clientToken: String? = null,
    val coreListener: JioMeetConnectionListener,
    val hostToken: String?,
    val jmJoinMeetingConfig: JMJoinMeetingConfig,
    val jmJoinMeetingData: JMJoinMeetingData
)