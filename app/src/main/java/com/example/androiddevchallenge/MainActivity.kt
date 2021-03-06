/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography
import androidx.compose.material.Icon as Icon

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TYLER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp(timerViewModel: TimerViewModel = viewModel()) {
    val isRunning by timerViewModel.isRunning.observeAsState(initial = false)

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.countdown_timer)) }) },
        floatingActionButton = {
            if (isRunning) {
                Stop(timerViewModel::stopTimer)
            } else {
                Start(timerViewModel::startTimer)
            }
        }
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val hours by timerViewModel.hours.observeAsState(initial = 0)
                val minutes by timerViewModel.minutes.observeAsState(initial = 0)
                val seconds by timerViewModel.seconds.observeAsState(initial = 0)
                val percent by timerViewModel.percent.observeAsState(initial = 1f)

                Welcome(hours, minutes, seconds)
                if (isRunning) {
                    Progress(percent)
                } else {
                    NumPad()
                }
            }
        }
    }
}

@Composable
fun Welcome(hours: Int, minutes: Int, seconds: Int) {
    Text(
        "${formatDigits(hours)}h ${formatDigits(minutes)}m ${formatDigits(seconds)}s",
        style = typography.h2,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

fun formatDigits(digit: Int): String {
    return digit.toString().padStart(2, '0')
}

@Composable
fun Progress(progress: Float) {
    val progressAnimated by animateFloatAsState(targetValue = progress)
    CircularProgressIndicator(
        progress = progressAnimated,
        strokeWidth = 8.dp,
        modifier = Modifier
            .width(300.dp)
            .height(300.dp)
            .padding(16.dp)
    )
}

@Composable
fun Start(onButtonPressed: () -> Unit) {
    FloatingActionButton(
        onClick = onButtonPressed,
        modifier = Modifier
            .width(72.dp)
            .height(72.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.start),
            contentDescription = stringResource(R.string.start),
            tint = Color.White,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
    }
}

@Composable
fun Stop(onButtonPressed: () -> Unit) {
    FloatingActionButton(
        onClick = onButtonPressed,
        modifier = Modifier
            .width(72.dp)
            .height(72.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.stop),
            contentDescription = stringResource(R.string.stop),
            tint = Color.White,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
    }
}

@Composable
fun NumPad() {
    Column {
        Row {
            Digit(1)
            Digit(2)
            Digit(3)
        }
        Row {
            Digit(4)
            Digit(5)
            Digit(6)
        }
        Row {
            Digit(7)
            Digit(8)
            Digit(9)
        }
        Row {
            Spacer(
                Modifier
                    .height(96.dp)
                    .width((72 + 8 + 8).dp)
            )
            Digit(0)
            Backspace()
        }
    }
}

@Composable
fun Digit(num: Int, timerViewModel: TimerViewModel = viewModel()) {
    OutlinedButton(
        onClick = { timerViewModel.onDigitPressed(num) },
        shape = RoundedCornerShape(percent = 50),
        modifier = Modifier
            .padding(8.dp)
            .width(72.dp)
            .height(72.dp)
    ) {
        Text(text = num.toString(), style = typography.h4)
    }
}

@Composable
fun Backspace(timerViewModel: TimerViewModel = viewModel()) {
    OutlinedButton(
        onClick = { timerViewModel.backspace() },
        shape = RoundedCornerShape(percent = 50),
        modifier = Modifier
            .padding(8.dp)
            .width(72.dp)
            .height(72.dp)
    ) {
        Text("DEL", style = typography.h6)
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

// @Preview("Dark Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun DarkPreview() {
//    MyTheme(darkTheme = true) {
//        MyApp()
//    }
// }
