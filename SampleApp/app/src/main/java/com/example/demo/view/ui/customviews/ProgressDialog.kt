package com.example.demo.view.ui.customviews

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.jio.sdksampleapp.R

@Preview
@Composable
fun ProgressDialog(message: String = stringResource(id = R.string.loading)) {

    Dialog(onDismissRequest = {}) {
        Surface(shadowElevation = 4.dp, shape = RoundedCornerShape(8.dp), color = Color.Gray) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AndroidView(factory = { context ->
                    ImageView(context).apply {
                        setImageResource(R.drawable.ic_spinner)
                        (drawable as? AnimationDrawable)?.start()
                    }
                }, modifier = Modifier.wrapContentSize())

                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}