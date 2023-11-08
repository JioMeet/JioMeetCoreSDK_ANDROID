package com.example.demo.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.model.MeetingIDError
import com.example.demo.model.MeetingPinError
import com.example.demo.model.SetJoinButtonEnabled
import com.example.demo.model.SetMeetingID
import com.example.demo.model.SetMeetingPin
import com.example.demo.model.SetUserName
import com.example.demo.model.UserNameError
import com.example.demo.viewModel.AppViewModel
import com.jio.sdksampleapp.R
import com.example.demo.view.ui.theme.background
import com.example.demo.view.ui.theme.buttonBackground

@Composable
fun CoreLoginView(onJoinMeetingClick: () -> Unit, viewModel: AppViewModel) {
    Surface(color = background) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.text_about_to_join_a_meeting),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(70.dp))
                CommonEditTextField(
                    textFieldValue = viewModel.loginState.value.meetingID,
                    textLabel = stringResource(id = R.string.hint_meeting_id),
                    textPlaceHolder = stringResource(id = R.string.enter_meeting_id),
                    isNumbersKeyboard = true,
                    onValueChanged = {
                        viewModel.onEvent(SetMeetingID(it))
                        viewModel.onEvent(MeetingIDError(false))
                        viewModel.onEvent(SetJoinButtonEnabled(true))
                    },
                    isError = viewModel.loginState.value.isMeetingIDError
                )
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(if (viewModel.loginState.value.isMeetingIDError) Color.Red else Color.White)
                )
                if (viewModel.loginState.value.isMeetingIDError) {
                    Text(
                        text = stringResource(id = R.string.meetingid_error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Left
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                CommonEditTextField(
                    textFieldValue = viewModel.loginState.value.meetingPin,
                    textLabel = stringResource(id = R.string.hint_meeting_pin),
                    textPlaceHolder = stringResource(id = R.string.enter_password),
                    isNumbersKeyboard = false,
                    onValueChanged = {
                        viewModel.onEvent(SetMeetingPin(it))
                        viewModel.onEvent(MeetingPinError(false))
                        viewModel.onEvent(SetJoinButtonEnabled(true))
                    },
                    isError = viewModel.loginState.value.isMeetingPinError
                )
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(if (viewModel.loginState.value.isMeetingPinError) Color.Red else Color.White)
                )
                if (viewModel.loginState.value.isMeetingPinError) {
                    Text(
                        text = stringResource(id = R.string.password_error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Left
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                CommonEditTextField(
                    textFieldValue = viewModel.loginState.value.userName,
                    textLabel = stringResource(id = R.string.username),
                    textPlaceHolder = stringResource(id = R.string.enter_name_hint),
                    isNumbersKeyboard = false,
                    onValueChanged = {
                        viewModel.onEvent(SetUserName(it))
                        viewModel.onEvent(UserNameError(false))
                        viewModel.onEvent(SetJoinButtonEnabled(true))
                    },
                    isError = viewModel.loginState.value.isUserNameError
                )
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(if (viewModel.loginState.value.isUserNameError) Color.Red else Color.White)
                )
                if (viewModel.loginState.value.isUserNameError) {
                    Text(
                        text = stringResource(id = R.string.username_error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Left
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))
                Button(
                    onClick = {
                        if (viewModel.isJoinButtonEnabled()) {
                            onJoinMeetingClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .height(40.dp)
                        .alpha(if (viewModel.loginState.value.isJoinButtonEnabled) 1f else 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = buttonBackground,
                        contentColor = Color.White,
                        disabledBackgroundColor = buttonBackground.copy(alpha = 0.5f)
                    ),
                    enabled = viewModel.loginState.value.isJoinButtonEnabled
                ) {
                    Text(
                        text = stringResource(id = R.string.join),
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = if (viewModel.loginState.value.isJoinButtonEnabled) 1f else 0.8f),
                        fontWeight = FontWeight.W900,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}