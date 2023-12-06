package com.example.demo.view.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.demo.helper.UserView
import com.example.demo.helper.BottomBarItems
import com.example.demo.helper.BottomItems
import com.example.demo.model.UserInfo
import com.example.demo.viewModel.JMClientViewModel


@Composable
fun MainScreen(viewModel: JMClientViewModel) {
    // Observe the remote users and local user data from the ViewModel
    val remoteUsers by viewModel.remoteUsers.collectAsState(emptyList())
    val localUser by viewModel.localUser.collectAsState(null)
    val screenShareInProgress by viewModel.screenShareInProgress.collectAsState()
    
    // Create a horizontally scrollable grid for remote users
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Create a horizontally scrollable grid for remote users
        RemoteUsersGrid(remoteUsers)

        // Display the local user in a big tile
        Column(modifier = Modifier.padding())     {
            localUser?.let { UserView(it,isLocalUser = true, 240.dp,240.dp)}
        }

        if(screenShareInProgress){
            Text("Screen Sharing in Progress")
        }
        // Create a bottom bar with audio and video mute icons
        BottomControlPanel(jmClientViewModel = viewModel, screenShareInProgress = screenShareInProgress)
    }
}

@Composable
fun RemoteUsersGrid(remoteUsers: List<UserInfo>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(remoteUsers) { user ->
            UserView(user, height = 140.dp,width = 140.dp)
        }
    }
}


@Composable
fun BottomControlPanel(
    modifier: Modifier = Modifier,
    jmClientViewModel: JMClientViewModel,
    screenShareInProgress: Boolean
) {
    val context = LocalContext.current
    val bottomBarItems by jmClientViewModel.bottomImageItems.collectAsState()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(5.dp),
        color = Color.Gray,
    ) {
        LazyRow(
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(bottomBarItems.size) { index ->
                val imageData = bottomBarItems[index]
                ShowRowItems(imageData, Modifier.clickable {
                    bottomBarActionListener(
                        jmClientViewModel,
                        imageData,
                        context,
                        screenShareInProgress
                    )
                })
            }
        }
    }
}
private fun bottomBarActionListener(
    jmClientViewModel: JMClientViewModel,
    imageData: BottomBarItems,
    context: Context,
    screenShareInProgress: Boolean
) {
    when (imageData.imageName) {
        BottomItems.Audio.name -> {
            jmClientViewModel.toggleMic(!jmClientViewModel.micStatus.value)
        }

        BottomItems.Video.name -> {
            jmClientViewModel.toggleCamera(!jmClientViewModel.camStatus.value)
        }

        BottomItems.Leave.name -> {
            jmClientViewModel.leaveCall()
        }
        BottomItems.ScreenShare.name -> {
            if(screenShareInProgress) {
                jmClientViewModel.toggleScreenShare(true)
            } else {
                //run foreground service
                jmClientViewModel.showScreenShareNotification(context)
                jmClientViewModel.toggleScreenShare(false)
            }
        }
    }
}

@Composable
fun ShowRowItems(
    item: BottomBarItems,
    modifier: Modifier,
) {
    Image(
        painterResource(
            id = (if (item.isSelected) item.imageEnable else (item.imageDisable))
        ),
        contentDescription = "Images",
        modifier = modifier
            .size(44.dp)
            .padding(10.dp)
    )
}
