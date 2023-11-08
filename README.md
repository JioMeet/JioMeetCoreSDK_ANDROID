# JioMeet Core SDK QuickStart

The JioMeet Core SDK is a powerful toolset that empowers developers to integrate high-quality audio
and video communication capabilities into their applications. Whether you're building a video
conferencing app, a virtual event platform, or anything that requires real-time audio and video
interactions, our SDK has you covered.

## Introduction

In this documentation, we'll guide you through the process of installation, enabling you to enhance
your Android app with Jiomeet's real-time communication capabilities swiftly and efficiently.Let's
get started on your journey to creating seamless communication experiences with Jiomeet Core SDK!

![image info](./images/core_flow.png)

---

## Prerequisites

Before you begin, ensure you have met the following requirements:

#### Hilt:

To set up Hilt in your Android project, follow these steps:

1. First, add the hilt-android-gradle-plugin plugin to your project’s root build.gradle file:

   ```gradle
   plugins {
   id("com.google.dagger.hilt.android") version "2.44" apply false
   }
   ```

2. Add the Hilt dependencies to the app-level build.gradle file

   ````gradle
   plugins {
     kotlin("kapt")
     id("com.google.dagger.hilt.android")
   }

   android {
       ...
       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_11
           targetCompatibility = JavaVersion.VERSION_11
       }
   }

   dependencies {
           implementation "androidx.hilt:hilt-navigation-compose:1.0.0"
           implementation "com.google.dagger:hilt-android:2.44"
           kapt "com.google.dagger:hilt-android-compiler:2.44"
   } ```
   ````

---

## Setup

##### Register on JioMeet Platform:

You need to first register on Jiomeet
platform.[Click here to sign up](https://platform.jiomeet.com/login/signUp)

##### Get your application keys:

Create a new app. Please follow the steps provided in
the [Documentation guide](https://dev.jiomeet.com/docs/quick-start/introduction) to create apps
before you proceed.

###### Get you Jiomeet meeting id and pin

Use
the [create meeting api](https://dev.jiomeet.com/docs/JioMeet%20Platform%20Server%20APIs/create-a-dynamic-meeting)
to get your room id and password

---

## Configure JioMeet Core SDK inside your app

i. In Gradle Scripts/build.gradle (Module: <projectname>) add the CORE dependency. The dependencies
section should look like the following:

```gradle
dependencies {
    ...
    implementation "com.jiomeet.platform:jiomeetcoresdk:<version>"
    ...
}
```

Find the [Latest version](https://github.com/JioMeet/JioMeetCoreSDK_ANDROID/releases) of the Core
SDK and replace <version> with the one you want to use. For example: 2.1.8.

### Add permissions for network and device access.

In /app/Manifests/AndroidManifest.xml, add the following permissions after </application>:

```gradle
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- The SDK requires Bluetooth permissions in case users are using Bluetooth devices. -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<!-- For Android 12 and above devices, the following permission is also required. -->
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

### Requesting run time permissions

it's crucial to request some permissions like **_CAMERA ,RECORD_AUDIO, READ_PHONE_STATE_** at
runtime since these are critical device access permissins to ensure a seamless and secure user
experience. Follow these steps

1. Check Permissions

```kotlin
if (checkPermissions()) {
  // Proceed with using the features.
} else {
  // Request critical permissions at runtime.
}
```

2. Request Runtime Permissions:

```kotlin
private void requestCriticalPermissions() {
  ActivityCompat.requestPermissions(
    this,
    new String []{
      Manifest.permission.READ_PHONE_STATE,
      Manifest.permission.CAMERA,
      Manifest.permission.RECORD_AUDIO
    },
    PERMISSION_REQUEST_CODE
  );

}
```

3. Handle Permission Results

```kotlin
@Override
public void onRequestPermissionsResult(
  int requestCode,
  @NonNull String[] permissions,
  @NonNull int[] grantResults
) {
  if (requestCode == PERMISSION_REQUEST_CODE) {
    if (areAllPermissionsGranted(grantResults)) {
      // Proceed with using the features that require critical permissions.
    } else {
      // Handle denied permissions, especially for camera and phone state, which are essential.
    }
  }
}
```

---

### Initialisation

we will be initialise the JMClient

```kotlin
// Import the necessary classes and packages

// 1. Create an instance of JMClientViewModel by injecting it with JMClient as a dependency.

// Example of injecting JMClientViewModel using Hilt:
@HiltViewModel
class JMClientViewModel @Inject constructor(private val jmClient: JMClient) : ViewModel() {
  init {
    jmClient.init()
    // Perform other setup operations here if needed
  }
}

class YourActivity : AppCompatActivity() {
  private val jmClientViewModel: JMClientViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_your)

    // JMClientViewModel is created and initialized upon first access.
    // The JMClient initialization and other setup operations will be executed in the ViewModel's constructor.
  }
}


```

---

- ## Meeting Management

  ##### **`joinMeeting`**

  allows you to join a meeting using the provided meeting data and configuration

  ```kotlin
  public final suspend fun joinMeeting(
      jmJoinMeetingData: JMJoinMeetingData,
      config: JMJoinMeetingConfig,
      roomDetailsWithPinResponse: (RoomDetailsWithPinResponse?) -> Unit
  ): Unit
  ```

  **Parameters**
  `JMJoinMeetingData` :The data required to join the meeting, including meeting ID, user ID, and
  username

  ```kotlin
  val joinMeetingData = JMJoinMeetingData(
      meetingId: "8591303436",
      meetingPin: "KkMK1",
      displayName: "John wick")
  ```

  `JMJoinMeetingConfig`:The configuration for joining the meeting, including audio, video settings
  and user role

  ```kotlin

  val jmJoinMeetingConfig = JMJoinMeetingConfig(
          userRole = userRole, //  // Specify the user role using JMUserRole (e.g., Host("host-token"), Speaker, Audience)
          isInitialAudioOn = true,
          isInitialVideoOn = true
      )
  ```

  `roomDetailsWithPinResponse` (Type: (RoomDetailsWithPinResponse?) -> Unit): A callback function
  that handles the room details with PIN response.This function is called after successfully joining
  the meeting and can be used to process room-related information.

  **Usage**

  ```kotlin
   jmClient.joinMeeting(jmJoinMeetingData, jmJoinMeetingConfig) {
          it?.let { roomDetails ->
              // Handle a successful meeting join
               // You can optionally set flags or perform other actions here
          }
      }
  ```

  ##### **`JoinRoom`**

  This method enables users to join a channel. Users in the same channel can talk to each other, and
  multiple users in the same channel can start a group chat

  ```kotlin
  public final fun joinRoom(): Unit
  ```

  **usage**
  Call this function after calling the joinMeeting when you want to join the room associated with
  the current meeting.

  ```
  // Example: Leave the meeting (host, cohost, or participant)
  jmClient.joinRoom()
  ```

  ##### **`leaveMeeting`**

  A suspend method that allows the host, cohost, or participant to leave the meeting. When called,
  this method exits the meeting session.

  ```kotlin
  public final suspend fun leaveMeeting(): Unit
  ```

  **usage**
  Call this method when you want to leave the ongoing meeting session. It can be used by the host,
  cohost, or any participant to exit the meeting.

  ```
  // Example: Leave the meeting (host, cohost, or participant)
  jmClient.leaveMeeting()
  ```

  ##### **`getCurrentMeeting`**

  A method that retrieves the details of the current meeting in progress. It provides information
  about the meeting, such as its unique identifier, topic, participants, and more.

  ```kotlin
  public final fun getCurrentMeeting(): JMMeeting
  ```

  **Returns**
  `JMMeeting` (Type: JMMeeting): An object containing details about the current meeting.

  **Usage**
  Call this method to retrieve information about the current meeting, including its participants,
  topic, and other relevant details

  ```kotlin
  // Example: Get details of the current meeting
      val currentMeeting = jmClient.getCurrentMeeting()
      val meetingId = currentMeeting.meetingId
      val meetingTopic = currentMeeting.meetingTopic
      val participants = currentMeeting.participants

      // Print meeting details
      println("Meeting ID: $meetingId")
      println("Meeting Topic: $meetingTopic")
      println("Participants: $participants")

  ```

---

- ## Meeting Controls

  ##### **`raiseHand`**

  A suspend method that allows a participant to raise or lower their hand during a meeting. This
  feature is commonly used in video conferencing to signal a desire to speak or ask a question.

  ```kotlin
  public final suspend fun raiseHand(isRaiseHand: Boolean): Unit
  ```

  **Parameters**
  `isRaiseHand` (Type: Boolean): Set to true to raise the participant's hand, indicating a desire to
  speak or ask a question. Set to false to lower the hand.
  **usage**

  ```
  // Example: Lower hand within a coroutine scope
  scope.launch {
      jmClient.raiseHand(false)
  }
  ```

  ##### **`lowerParticipantHand`**

  A suspend method that allows the host or cohost to lower the hand of a specific participant in the
  meeting. This action removes the hand-raised status of the specified participant.

  ```kotlin
  public final suspend fun lowerParticipantHand(participantId: String): Unit
  ```

  **Parameters**
  `participantId` (Type: String): The unique identifier of the participant whose hand you want to
  lower.
  **usage**
  Call this method when the host or cohost wants to lower the hand of a specific participant who has
  raised their hand during the meeting.

  ```
     // Example: Lower the hand of a specific participant (host or cohost)
  val participantIdToLowerHand = "participant123"
  jmClient.lowerParticipantHand(participantIdToLowerHand)

  ```

  ##### **`lowerAllParticipantHand`**

  A suspend method that allows the host or cohost to lower the hand of all participants in the
  meeting. This action removes the hand-raised status from all participants.

  ```kotlin
  public final suspend fun lowerAllParticipantHand(): Unit
  ```

  **usage**
  Call this method when the host or cohost wants to lower the hand of all participants who have
  raised their hands during the meeting.

  ```
  // Example: Lower hand within a coroutine scope
  scope.launch {
     // Example: Lower the hand of all participants (host or cohost)
      jmClient.lowerAllParticipantHand()
  }
  ```

  ##### **`OnUpdateParticipantType`**

  allows the host or cohost to update the type of a participant during a meeting. This method is
  typically used to change a participant's role, such as switching them from a speaker to an
  audience member or vice versa.

  ```kotlin
  public final fun OnUpdateParticipantType(participantId: String, isSpeakerToAudience: Boolean): Unit
  ```

  **Parameters**
  `participantId` (Type: String): The unique identifier of the participant whose type is being
  updated.
  `isSpeakerToAudience` (Type: Boolean): Set to true to update the participant as a speaker to the
  audience or false to change their type to another role.

  **Usage**
  Call this method to update the type of a participant during a meeting. Typically, this method is
  called by the host or cohost to change a participant's role from speaker to audience or vice
  versa.

  ```
  // Example: Update a participant's type to speaker
  jmClient.OnUpdateParticipantType("participant123", true)

  // Example: Update a participant's type to audience
  jmClient.OnUpdateParticipantType("participant456", false)

  ```

  ##### **`recordingStart`**

  A suspend method that allows you to initiate the recording of a meeting. This method starts the
  recording process, capturing audio and video from the meeting participants.

  ```kotlin
  public final suspend fun recordingStart(): Unit
  ```

  **Usage**
  Call this method to start recording a meeting. Once initiated, the recording process will capture
  audio and video from the meeting participants. You can use this method to record important
  meetings or presentations for later reference.

  ```Kotlin
      jmClient.recordingStart()
  ```

  ##### **`recordingStop`**

  A suspend method that allows you to stop the recording of a meeting. This method ends the
  recording process and finalizes the recorded content.

  ```kotlin
  public final suspend fun recordingStop(): Unit
  ```

  **Usage**
  Call this method to stop the recording of a meeting that was previously initiated using the
  recordingStart method. Stopping the recording finalizes the recorded content, making it available
  for playback or storage.

  ```Kotlin
      jmClient.recordingStop()
  ```

  ##### **`screenShareState`**

  A method that allows you to control the screen sharing state during a meeting. This method can be
  used to start or stop screen sharing.

  ```kotlin
     public final fun screenShareState(isScreenShare: Boolean): Unit
  ```

  **Parameters**
  isScreenShare (Type: Boolean): Set to true to start or resume screen sharing, or false to stop
  screen sharing.
  **Usage**
  Call this method to control the screen sharing state during a meeting. You can use it to start or
  stop screen sharing as needed.

  ```Kotlin
  // Example: Start screen sharing
  jmClient.screenShareState(true)

  // Example: Stop screen sharing
  jmClient.screenShareState(false)
  ```

  ##### **`softMuteAllParticipantsAudio`**

  A suspend method that allows host/co-host to softly mute or unmute the audio of all participants
  in a meeting. Soft muting means that participants can still unmute themselves if needed.

  ```kotlin
     public final suspend fun softMuteAllParticipantsAudio(isMuted: Boolean): Unit
  ```

  **Parameters**
  `isMuted` (Type: Boolean): Set to true to softly mute all participants' audio, or false to unmute
  their audio.
  **Usage**
  Call this method to softly mute or unmute the audio of all participants in a meeting. Soft muting
  allows participants to have control over their audio and unmute themselves if necessary.

  ```Kotlin
     // Example: Soft mute all participants' audio
  jmClient.softMuteAllParticipantsAudio(true)

  // Example: Unmute all participants' audio
  jmClient.softMuteAllParticipantsAudio(false)

  ```

  ##### **`hardMuteAllParticipantsAudio`**

  A suspend method that allows the host or cohost to hard mute or unmute the audio of all
  participants in the meeting. When called, this method forcefully mutes or unmutes the audio of all
  participants.

  ```kotlin
     public final suspend fun hardMuteAllParticipantsAudio(isMuted: Boolean): Unit
  ```

  **Parameters**
  `isMuted` (Type: Boolean): Set to true to hardly mute all participants' audio, or false to unmute
  their audio.
  **Usage**
  Call this method when the host or cohost wants to forcefully mute or unmute the audio of all
  participants in the meeting.

  ```Kotlin
     // Example: Hard mute all participants' audio
  jmClient.hardMuteAllParticipantsAudio(true)

  // Example: Unmute all participants' audio
  jmClient.hardMuteAllParticipantsAudio(false)

  ```

  ##### **`hardMuteAllParticipantsVideo`**

  A suspend method that allows the host or cohost to hard mute or unmute the Video of all
  participants in the meeting. When called, this method forcefully mutes or unmutes the Video of all
  participants.

  ```kotlin
     public final suspend fun hardMuteAllParticipantsAudio(isMuted: Boolean): Unit
  ```

  **Parameters**
  `isMuted` (Type: Boolean): Set to true to hardly mute all participants' Video, or false to unmute
  their Video.
  **Usage**
  Call this method when the host or cohost wants to forcefully mute or unmute the Video of all
  participants in the meeting.

  ```Kotlin
     // Example: Hard mute all participants' video
  jmClient.hardMuteAllParticipantsVideo(true)

  // Example: Unmute all participants' video
  jmClient.hardMuteAllParticipantsVideo(false)

  ```

  ##### **`softMuteAllParticipantsVideo`**

  A suspend method that allows you to softly mute or unmute the video of all participants in a
  meeting. Soft muting means that participants can still unmute themselves if needed.

  ```kotlin
     public final suspend fun softMuteAllParticipantsVideo(isMuted: Boolean): Unit
  ```

  **Parameters**
  `isMuted` (Type: Boolean): Set to true to softly mute all participants' audio, or false to unmute
  their video.
  **Usage**
  Call this method to softly mute or unmute the audio of all participants in a meeting. Soft muting
  allows participants to have control over their video and unmute themselves if necessary.

  ```Kotlin
     // Example: Soft mute all participants' video
  jmClient.softMuteAllParticipantsVideo(true)

  // Example: Unmute all participants' audio
  jmClient.softMuteAllParticipantsVideo(false)

  ```

  ##### **`startWhiteboardSharing`**

  A suspend method that allows you to initiate whiteboard sharing during a meeting. This method
  starts the whiteboard sharing process, enabling participants to collaborate and draw on a shared
  whiteboard.

  ```kotlin
    public final suspend fun startWhiteboardSharing(jiomeetId: String, participantUri: String, historyId: String): Unit

  ```

  **Parameters**
  `jiomeetId` (Type: String): The unique identifier of the whiteboard sharing session within the
  meeting.
  You can obtain this from `jmClient.getCurrentMeeting().meetingId`.
  `participantUri` (Type: String): The unique identifier of the participant who is initiating
  whiteboard sharing.You can obtain this from `jmClient.getCurrentParticipantUri().`
  `historyId` (Type: String): The identifier associated with the history or version of the
  whiteboard content. You can obtain this from `jmClient.getHistoryId().`

  **Usage**
  Call this method to initiate whiteboard sharing during a meeting. When whiteboard sharing is
  started, participants can collaborate, draw, and share content on the shared whiteboard.

  ```Kotlin
           // Example: Start whiteboard sharing
      val jiomeetId = jmClient.getCurrentMeeting().meetingId
      val participantUri = jmClient.getCurrentParticipantUri()
      val historyId = jmClient.getHistoryId()
      jmClient.startWhiteboardSharing(jiomeetId, participantUri, historyId)
  ```

  ##### **`stopWhiteboardSharing`**

  A suspend method that allows you to stop the whiteboard sharing session during a meeting. This
  method ends the whiteboard sharing, disabling further collaboration and drawing on the shared
  whiteboard.

  ```kotlin
     public final suspend fun startWhiteboardSharing(jiomeetId: String, participantUri: String, historyId: String): Unit
  ```

  **Usage**
  Call this method to stop the whiteboard sharing session during a meeting. Stopping whiteboard
  sharing will disable further collaboration and drawing on the shared whiteboard.

  ```Kotlin
  // Example: Stop whiteboard sharing
  jmClient.stopWhiteboardSharing()
  ```

  ##### **`onCameraRequestDecline`**

  A method that allows you to decline a camera request from host/cohost.

  ```kotlin
     public final fun onCameraRequestDecline(targetParticipantUri: String): Unit
  ```

  **Parameters**
  `targetParticipantUri` (Type: String): The unique identifier (URI) of the participant whose camera
  request you want to decline.

  **Usage**
  Call this method when you want to decline a camera request from a specific participant.

  ```Kotlin
    // Example: Remove a participant with the specified participantId
  val targetParticipantUri = "participant456"
  jmClient.onCameraRequestDecline(targetParticipantUri)
  ```

  ##### **`onMicRequestDecline`**

  A method that allows you to decline a camera request from host/cohost.

  ```kotlin
     public final fun onMicRequestDecline(targetParticipantUri: String): Unit
  ```

  **Parameters**
  `targetParticipantUri` (Type: String): The unique identifier (URI) of the participant whose camera
  request you want to decline.

  **Usage**
  Call this method when you want to decline a Mic request from a specific participant.

  ```Kotlin
    // Example: Remove a participant with the specified participantId
    val targetParticipantUri = "participant456"
  jmClient.onMicRequestDecline(targetParticipantUri)
  ```

  ##### **`muteParticipantAudio`**

  A suspend method that allows the host or cohost to mute or unmute the audio of a specific
  participant in the meeting. You can use this method to control the audio state of a participant,
  muting or unmuting their audio stream.

  ```kotlin
     public final suspend fun muteParticipantAudio(participantId: String, isMuted: Boolean): Unit
  ```

  **Parameters**
  `participantId` (Type: String): The unique identifier of the participant whose audio you want to
  mute or unmute.
  `isMuted` (Type: Boolean): Set to true to mute the audio of the specified participant, or false to
  unmute it.

  **Usage**
  Call this method when the host or cohost needs to mute or unmute the audio of a specific
  participant during a meeting. It allows you to control the audio experience for individual
  participants.

  ```Kotlin
      // Example: Mute the audio of a specific participant (host or cohost)
  val participantIdToMute = "participant123"
  jmClient.muteParticipantAudio(participantIdToMute, true)

  // Example: Unmute the video of a specific participant (host or cohost)
  val participantIdToUnmute = "participant456"
  jmClient.muteParticipantAudio(participantIdToUnmute, false)
  ```

  ***

  ##### **`muteParticipantVideo`**

  A suspend method that allows the host or cohost to mute or unmute the video of a specific
  participant in the meeting. You can use this method to control the video state of a participant,
  muting or unmuting their video stream.

  ```kotlin
     public final suspend fun muteParticipantVideo(participantId: String, isMuted: Boolean): Unit
  ```

  **Parameters**
  `participantId` (Type: String): The unique identifier of the participant whose video you want to
  mute or unmute.
  `isMuted` (Type: Boolean): Set to true to mute the video of the specified participant, or false to
  unmute it.

  **Usage**
  Call this method when the host or cohost needs to mute or unmute the video of a specific
  participant during a meeting. It allows you to control the video experience for individual
  participants.

  ```Kotlin
      // Example: Mute the video of a specific participant (host or cohost)
  val participantIdToMute = "participant123"
  jmClient.muteParticipantVideo(participantIdToMute, true)

  // Example: Unmute the video of a specific participant (host or cohost)
  val participantIdToUnmute = "participant456"
  jmClient.muteParticipantVideo(participantIdToUnmute, false)
  ```

  ***

* ## Audio management
  ##### **`playbackSignalVolume Method`**
  Allows you to adjust the playback signal volume of all remote users in a meeting. This method is
  useful for controlling the audio playback volume for participants' audio signals.
  ```kotlin
         public final fun playbackSignalVolume(volume: Int): Unit
  ```
  **Parameters**
  `volume` (Type: Int): An integer value representing the desired playback signal volume. The volume
  level can typically range from 0 (mute) to 400 (maximum volume).
  **Usage**
  ```kotlin
      // Example: Adjust the playback signal volume for all remote users
      jmClient.playbackSignalVolume(80) // Set the volume level to 80 (80% of maximum)
  ```

  ##### **`setAuidioOnlyMode`**
  Allows you to set the local particpant in audioOnly mode. This method provides the audio only while user has opened his video, which can help optimize bandwidth usage and
  improve the overall meeting experience.
  ```kotlin
         public final fun setAudioOnlyMode(isAudioOnlyMode: Boolean): Unit
  ```
  **Parameters**
  `isAudioOnlyMode` (Type: Boolean):Set to true to apply audioOnly mode, or false to unmute it.

  ---
* ## Video management
  ##### **`setVideoStreamQuality`**
  Allows you to set the video stream quality for specific participants in a meeting. This method
  provides control over the video quality settings, which can help optimize bandwidth usage and
  improve the overall meeting experience.

  **Parameters**
  `videoQualityMap` (Type: Map<String, Any>): A map that specifies the video stream quality settings
  for specific participants. The keys in the map represent participant identifiers (e.g., user IDs
  or participant IDs), and the values indicate the desired video quality settings for each
  participant.

  **Usage**
  ```kotlin
      / Define video quality settings for participants
      val videoQualityMap = mapOf(
          "user123" to VideoQuality.HIGH,
          "user456" to VideoQuality.LOW
      )
      // Apply the video quality settings
      jmClient.setVideoStreamQuality(videoQualityMap)
  ```

---

- ## Subscribing to audio and video streams

  ##### **`muteLocalAudio`**

  a suspend method that allows you to mute or unmute your local audio during a meeting. When called
  with `true`, it mutes your local audio, and when called with `false`, it unmutes your local audio.

  ```kotlin
  public final suspend fun muteLocalAudio(isMute: Boolean): Unit
  ```

  **Parameters:**
  `isMute` (Type: Boolean): Set to true to mute your local audio, or false to unmute it.

  **Usage**
  Call this method when you want to control the state of your local audio during a meeting. Muting
  your local audio means that other meeting participants will not hear your audio, while unmuting it
  allows you to speak and be heard by others.

  ```kotlin
  // Example: Mute your local audio
  jmClient.muteLocalAudio(true)

  // Example: Unmute your local audio
  jmClient.muteLocalAudio(false)

  ```

  ##### **`muteLocalVideo`**

  A suspend method that allows you to mute or unmute your local Video during a meeting. When called
  with `true`, it mutes your local Video, and when called with `false`, it unmutes your local Video.

  ```kotlin
  public final suspend fun muteLocalVideo(isMute: Boolean): Unit
  ```

  **Parameters:**
  `isMute` (Type: Boolean): Set to true to mute your local Video, or false to unmute it.

  **Usage**
  Call this method when you want to control the state of your local Video during a meeting. Muting
  your local Video means that other meeting participants will not hear your Video, while unmuting it
  allows you to speak and be heard by others.

  ```kotlin
  // Example: Mute your local Video
  jmClient.muteLocalVideo(true)

  // Example: Unmute your local audio
  jmClient.muteLocalVideo(false)

  ```

  ##### **`subscribeRemoteUserVideo`**

  controls the subscription status of a remote user's video stream.

  ```kotlin
  public final fun subscribeRemoteUserVideo(userId: Int, subscribe: Boolean): Unit
  ```

  **Parameters:**

  - `userId` (Type: `Int`): The unique identifier of the remote user.
  - `subscribe` (Type: `Boolean`): Set to `true` to start or resume subscribing to the remote
    user's video, or `false` to stop or pause the subscription.

  **Usage**
  Call this method after successfully joining a meeting to control video stream subscription for
  remote users.

  ```kotlin
  // Example: Subscribe to a remote user's video stream
  jmClient.subscribeRemoteUserVideo(123, true)

  // Example: Unsubscribe from a remote user's video stream
  jmClient.subscribeRemoteUserVideo(456, false)
  ```

  ##### **`subscribeRemoteUserAudio`**

  controls the subscription status of a remote user's audio stream.

  ```kotlin
  public final fun subscribeRemoteUserAudio(userId: Int, subscribe: Boolean): Unit
  ```

  **Parameters**

  - `userId` (Type: `Int`): The unique identifier of the remote user.
  - `subscribe` (Type: `Boolean`): Set to `true` to start or resume subscribing to the remote
    user's video, or `false` to stop or pause the subscription.

  **Usage**
  Call this method after successfully joining a meeting to control audio stream subscription for
  remote users.

  ```kotlin
  // Example: Subscribe to a remote user's video stream
  jmClient.subscribeRemoteUserAudio(123, true)

  // Example: Unsubscribe from a remote user's video stream
  jmClient.subscribeRemoteUserAudio(456, false)
  ```

* ### Virtual background
  `applyVirtualBackground` is a method that allows you to apply a virtual background to the video
  stream.Once the virtual background feature is enabled, all users in the channel can see the custom
  background.
  ```kotlin
  public final fun applyVirtualBackground(virtualBackgroundType: VirtualBackgroundType): Unit
  ```
  **Parameters**
  - `virtualBackgroundType` (Type: `VirtualBackgroundType`): The type of virtual background to
    apply.

    **`VirtualBackgroundType`**
    VirtualBackgroundType is an enumeration representing different types of virtual backgrounds
    that can be applied to a video stream.

    **Enum Values**

    - `NONE`: Represents no virtual background.
    - `BLUR`: Represents a blurred virtual background.
    - `IMAGE`: Represents a virtual background using an image.

    **Properties**

    - `imagePath` (Type: `String?`): A property associated with each enum value. It stores the
      path or URL of a custom image used as a virtual background when the `IMAGE` type is
      selected.
      **Usage**
      You can use the `VirtualBackgroundType` enumeration to specify the type of virtual
      background to apply when using the `applyVirtualBackground` method in your application.
  ```kotlin
  // Example: Apply a blurred virtual background
  jmClient.applyVirtualBackground(VirtualBackgroundType.BLUR)

  // Example: Remove virtual background
  jmClient.applyVirtualBackground(VirtualBackgroundType.NONE)

  // Example: Apply a virtual background with a specific image path
    val type = VirtualBackgroundType.IMAGE
    type.imagePath = "https://example.com/custom-background.jpg"
    jmClient.applyVirtualBackground(VirtualBackgroundType.IMAGE)
  ```
* ### Chat
  ##### **`fetchNextChatMessage`**
  A suspend method that retrieves the next chat message in a meeting's chat thread. This method is
  used to fetch and load additional chat messages.
  ```kotlin
  public final suspend fun fetchNextChatMessage(): Unit
  ```
  **Usage**
  You can call this method initially to fetch the initial set of chat messages, and then call it
  again when you need to load more chat messages, such as when the user scrolls through the chat
  thread.
  ```kotlin
    // Example: Fetch the next chat message
  if (!state.isLoading && !state.endReached && state.items.isNotEmpty()) {
      jmClient.fetchNextChatMessage()
  }
  //where state = jmClient.state
  ```
  ##### **`sendChatMessage`**
  A suspend method that allows users to send a chat message in the meeting. It sends the specified
  message to the chat thread.
  ```kotlin
      public final suspend fun sendChatMessage(message: String): Unit
  ```
  **Parameters**
  `message` (Type: String): The text message to be sent in the chat.
  **Usage**
  Call this method to send a chat message in the meeting's chat thread.
  ```kotlin
    // Example: Send a chat message
  jmClient.sendChatMessage("Hello, everyone!")
  ```
  ##### **`ScreenState`**
  A Data class that represents the state of the screen during a meeting. It encapsulates information
  such as whether the screen is loading, the chat message items, any error messages, and more.
  **Properties**
  ```kotlin
     public final data class ScreenState(
     //Indicates whether the screen is currently in a loading state.
      val isLoading: Boolean,
      //Contains a list of chat messages displayed on the screen.
      val items: MutableList<ChatMessage>,
      //Represents any error message, if applicable.
      val error: String?,
      //Indicates whether the end of the chat thread has been reached.
      val endReached: Boolean,
      //The offset value for fetching additional chat messages.
      val offset: Int,
      // The total number of chat messages.
      val itemCount: Int
  )
  ```

---

- ## Observe Meeting Events
  various events received when using the `jmClient.observeJmClientEvent()`. These events represent
  different interactions and changes that can occur within a meeting.
  ## OnUserFailedToJoinMeeting
  This event is triggered when a user fails to join a meeting.
  Properties:
  - `error` (Type: `JMMeetingJoinError`): The error encountered when joining the meeting.
  ## OnUserJoinMeeting
  This event is triggered when a user successfully joins a meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the user who joined.
  - `currentRoom` (Type: `JMCurrentRoom`): Information about the current room.
  ## OnUserMicStatusChanged
  This event is triggered when a user's microphone status changes (mute/unmute).
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the user whose mic status changed.
  - `isMuted` (Type: `Boolean`): Indicates whether the user's microphone is muted.
  ## OnUserVideoStatusChanged
  This event is triggered when a user's video status changes (start/stop video).
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the user whose video status changed.
  - `isMuted` (Type: `Boolean`): Indicates whether the user's video is muted.
  ## OnUserHandRaiseStatusChanged
  This event is triggered when a user raises or lowers their hand in the meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the user whose hand-raise status changed.
  - `isRaised` (Type: `Boolean`): Indicates whether the user's hand is raised.
  ## OnUserLeaveMeeting
  This event is triggered when a user leaves the meeting.
  ## OnRemoteUserJoinMeeting
  This event is triggered when a remote user joins the meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the remote user who joined.
  ## OnRemoteUserMicStatusChanged
  This event is triggered when a remote user's microphone status changes (mute/unmute).
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the remote user whose mic status changed.
  - `isMuted` (Type: `Boolean`): Indicates whether the remote user's microphone is muted.
  ## OnRemoteUserVideoStatusChanged
  This event is triggered when a remote user's video status changes (start/stop video).
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the remote user whose video status changed.
  - `isMuted` (Type: `Boolean`): Indicates whether the remote user's video is muted.
  ## OnRemoteUserHandRaiseStatusChanged
  This event is triggered when a remote user raises or lowers their hand in the meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the remote user whose hand-raise status
    changed.
  - `isRaised` (Type: `Boolean`): Indicates whether the remote user's hand is raised.
  ## OnLowerAllHandParticipant
  This event is triggered when a user lowers all raised hands in the meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  ## OnRemoteUserLeftMeeting
  This event is triggered when a remote user leaves the meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the remote user who left.
  - `isMovedToAudience` (Type: `Boolean`): Indicates whether the remote user was moved to the
    audience.
  ## OnHostHardAudioMuteStatusChange
  This event is triggered when the host's hard audio mute status changes.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `host` (Type: `JMMeetingUser`): Information about the host.
  - `isMuted` (Type: `Boolean`): Indicates whether the host's audio is hard-muted.
  ## OnHostSoftAudioMuteStatusChange
  This event is triggered when the host's soft audio mute status changes.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `host` (Type: `JMMeetingUser`): Information about the host.
  - `isMuted` (Type: `Boolean`): Indicates whether the host's audio is soft-muted.
  ## OnHostLowerAllRaisedHands
  This event is triggered when the host lowers all raised hands in the meeting.
  Properties:
  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `host` (Type: `JMMeetingUser`): Information about the host.
  ## OnMeetingDataRefreshed
  This event is triggered when meeting data is refreshed.
  Properties:

* `meeting` (Type: `JMMeeting`): Information about the meeting.

  ## OnNetworkQualityChanges

  This event is triggered when the network quality changes.

  Properties:

  - `networkQuality` (Type: `JMNetworkQuality`): Information about the network quality.

  ## OnActiveSpeaker

  This event is triggered when there is an active speaker in the meeting.

  Properties:

  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the active speaker (can be `null`).

  ## OnTopSpeaker

  This event is triggered when the loudest participant is a local SDK user.

  Properties:

  - `activeParticipantUId` (Type: `String`): The user ID of the loudest participant.

  ## LoudestParticipantIsLocalSDK

  This event is triggered when the loudest participant is a local SDK user.

  Properties:

  - `isLocalParticipantIsSpeaking` (Type: `Boolean`): Indicates whether the local participant is
    speaking.
  - `numberOfLoudestSpeakers` (Type: `Int`): The number of loudest speakers.
  - `listActiveParticipant` (Type: `MutableList<ActiveParticipant>`): List of active participants.
  - `totalVolume` (Type: `Int`): Total volume of active participants.

  ## OnUserSpeakingWhileMute

  This event is triggered when a user continues to speak while muted.

  ## OnCoHostStatusUpdate

  This event is triggered when the co-host status is updated.

  Properties:

  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `host` (Type: `JMMeetingUser`): Information about the co-host (can be `null`).

  ## OnParticipantRefresh

  This event is triggered when participant data is refreshed.

  Properties:

  - `meeting` (Type: `JMMeeting`): Information about the meeting.

  ## OnParticipantMovedToAudience

  This event is triggered when a participant is moved to the audience.

  Properties:

  - `meeting` (Type: `JMMeeting`): Information about the meeting.

  ## OnHostDisconnectedParticipant

  This event is triggered when the host disconnects a participant.

  ## OnParticipantMovedToSpeaker

  This event is triggered when a participant is moved to the speakers.

  Properties:

  - `meeting` (Type: `JMMeeting`): Information about the meeting.
  - `user` (Type: `JMMeetingUser`): Information about the participant who was moved to the
    speakers.

  ## OnChatMessageReceived

  This event is triggered when a chat message is received.

  ## OnRtcStatsReceived

  This event is triggered when RTC (Real-Time Communication) statistics are received.

  Properties:

  - `rtcStats` (Type: `NetworkInfoStats`): Information about RTC statistics.

  ## OnScreenShareStarted

  This event is triggered when screen sharing is started.

  Properties:

  - `remoteView` (Type: `RemoteView`): The remote view for screen sharing.

  ## OnScreenShareStop

  This event is triggered when screen sharing is stopped.

  Properties:

  - `isScreenShareStop` (Type: `Boolean`): Indicates whether screen sharing has stopped.

  ## LocalScreenShareStart

  This event is triggered when local screen sharing is started.

  Properties:

  - `isLocalScreenShareStart` (Type: `Boolean`): Indicates whether local screen sharing has
    started.

  ## LocalScreenShareStop

  This event is triggered when local screen sharing is stopped.

  Properties:

  - `isLocalScreenShareStop` (Type: `Boolean`): Indicates whether local screen sharing has
    stopped.

  ## OnScreenShareEventsMessages

  This event is triggered when there are screen sharing events messages.

  Properties:

  - `eventMessage` (Type: `ScreenShareEventsMessages`): Information about the screen sharing
    event.

  ## OnAskParticipantMicUnmute

  This event is triggered when a participant is asked to unmute their microphone.

  Properties:

  - `targetHostUserId` (Type: `String`): The user ID of the target participant.

  ## OnAskParticipantCameraUnmute

  This event is triggered when a participant is asked to unmute their camera.

  Properties:

  - `targetHostUserId` (Type: `String`): The user ID of the target participant.

## OnHeadSetConnected

This event is triggered when there is external device state and audio device type.

Properties:

     `eventMessage` (Type: `HeadsetStateChangedEvent`): Information about the device type state and audio device type.

## OnParticipantDeclinedMicRequest

This event is triggered when a participant declines a microphone request.

Properties:

- `user` (Type: `JMMeetingUser`): Information about the participant who declined the request.

## OnParticipantDeclinedCameraRequest

This event is triggered when a participant declines a camera request.

Properties:

- `user` (Type: `JMMeetingUser`): Information about the participant who declined the request.

## OnToggleAudioOnlyMode

This event is triggered when a participant is in audio only mode.

Properties:

- `isAudioOnlyEnabled` (Type: `Boolean`): Indicates whether participant is on audio only mode.

## OnWhiteBoardStatusUpdate

This event is triggered when the whiteboard status is updated.

Properties:

- `isStarted` (Type: `Boolean`): Indicates whether the whiteboard has started.
- `whiteboardModel` (Type: `WhiteboardModel`): Information about the whiteboard.

## OnRecordingStatusChanged

This event is triggered when the recording status changes.

Properties:

- `isRecording` (Type: `Boolean`): Indicates whether recording is active.

**Usage**

  ```Kotlin
  jmClient.observeJmClientEvent().collect { event ->
  when (event) {
    is OnRemoteUserJoinMeeting -> {
      // Handle when a remote user joins the meeting
      //participant = event.meeting.participants
    }

    else -> {
      // Handle other events if necessary
    }
  }
}

  ```

---

- ## Data classes and enums

  `JMMeetingUser`

  ```kotlin
  data class JMMeetingUser(var uid: String, var userId: String, var displayName: String) {
     var isHost: Boolean = false
     var isCoHost: Boolean = false
     var isSpeaker: Boolean = false
     var isAudioMuted: Boolean = false
     var isVideoMuted: Boolean = false
     var isHandRaised: Boolean = false
     var isLoudest: Boolean = false
     var isSharingScreen: Boolean = false
     var rendererView: RenderView? = null
     var participantType: String = Constant.ParticipantType.TYPE_SPEAKER
     var isRecording:Boolean = false
  }
  ```

  `JMMeeting`

  ```kotlin
  data class JMMeeting(
  val meetingId: String,
  val meetingPin: String,
  val meetingTopic: String,
  val roomId: String,
  val participants: List<JMMeetingUser>,
  val localParticipant: List<JMMeetingUser>,
  val remoteParticipants: List<JMMeetingUser>,
  val isHardAudioMute: Boolean
  )
  ```

  `JMNetworkQuality`

  ```
  enum class JMNetworkQuality {
  GOOD,
  BAD,
  POOR,
  DETECTING,
  }
  ```

  `NetworkInfoStats`

  ```kotlin
      data class NetworkInfoStats(
      var Latency: Int = 0,
      var txPacketLoss: Int = 0,
      var rxPacketLoss: Int = 0,
  )
  ```

  `JMCurrentRoom`

  ```kotlin
  data class JMCurrentRoom(val stopScreenShare: (Unit) -> Unit){
   var meetingId: String = ""
   var meetingPin: String = ""
   var userDisplayName: String = ""
   var roomID: String = ""
   var webRtcAppId: String = ""
   var userWebRtcId: String = ""
   var userWebRtcToken: String = ""
   var userWebRtmToken: String = ""
   var meetingOwnerUserId: String = ""
   var meetingOwnerName: String = ""
   var meetingTitle: String = ""
   var historyId: String = ""
   var chatThreadId: String = ""
   var roomUrl: String = ""
   var mediaEngine:String = ""

   var guestUserId: String = ""
   var isGuestUserHost: Boolean = false

   var isCurrentUserAudioMuted = false
   var isCurrentUserVideoMuted = false
   var isCurrentUserHandRaised = false
   var isCurrentUserSharingScreen = false
   var currentParticipantType = Constant.ParticipantType.TYPE_SPEAKER


   var isHardMuteEnabled = false
   var isSoftMuteEnabled = false

   internal var participantsMap: MutableMap<String, JMMeetingUser> = mutableMapOf()

   var deviceId: String = ""
   var meetingUrl:String = ""
   var screenShareAgoraToken:String  = ""
   var screenShareAgoraUid:String  = ""
   var isWhiteboardShared:Boolean  = false
   var isLocalUserSharedWhiteboard:Boolean  = false
   var isRecording = false
   }
  ```

  `JMMeetingJoinError` is a sealed interface representing possible errors that can occur during the
  process of joining a meeting. Below are its subtypes:

  - `InvalidConfigurationError`: Indicates that the configuration for joining the meeting is
    invalid.

  - `InvalidMeetingDetails`: Indicates that the provided meeting details are invalid.

  - `MeetingExpired`: Indicates that the meeting has expired and cannot be joined.

  - `FailedToRegister`: Indicates a failure to register for the meeting.

  - `FailedToJoinCall`: Represents an error with a custom error message when joining the meeting.

---

## Troubleshooting

- Facing any issues while integrating or installing the JioMeet Template UI Kit please connect with
  us via real time support present in jiomeet.support@jio.com
  or https://jiomeetpro.jio.com/contact-us
