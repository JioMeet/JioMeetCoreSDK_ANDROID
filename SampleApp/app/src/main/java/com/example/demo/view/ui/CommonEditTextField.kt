package com.example.demo.view.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonEditTextField(
    textFieldValue: String,
    textLabel: String,
    textPlaceHolder: String,
    isNumbersKeyboard: Boolean,
    onValueChanged: (String) -> Unit,
    isError: Boolean
) {
    TextField(
        value = textFieldValue, onValueChange = {
            onValueChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
        ),
        isError = isError,
        label = {
            Text(
                text = textLabel,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(5.dp))
        },
        placeholder = {
            Text(
                text = textPlaceHolder,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Left
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Next,
            keyboardType = if (isNumbersKeyboard) KeyboardType.Number else KeyboardType.Text
        ),
        singleLine = true
    )
}