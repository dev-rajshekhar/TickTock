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
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.Color1
import com.example.androiddevchallenge.ui.theme.Color2
import com.example.androiddevchallenge.ui.theme.Color3
import com.example.androiddevchallenge.ui.theme.Color4
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
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
fun MyApp(vm: CounterVm = viewModel()) {
    val progress: Long by vm.time.observeAsState(0L)
    val percentage = remember { mutableStateOf(0f) }
    percentage.value = ((30 - progress) / 30f)

    val transition = updateTransition(progress)
    val color by transition.animateColor() { state ->
        when (state.toInt()) {
            in 1..6 -> Color4
            in 7..15 -> Color3
            in 16..22 -> Color2
            in 23..30 -> Color1
            else -> Color1
        }
    }

    val opacity by transition.animateFloat() { state ->
        when (state.toInt()) {
            in 0..1 -> 0f
            in 2..3 -> .2f
            in 4..6 -> .3f
            in 7..15 -> .4f
            in 16..22 -> .6f
            in 23..26 -> .7f
            in 27..30 -> .9f
            else -> 1f
        }
    }
    Countdown(
        percentage = percentage.value,
        opacity = opacity,
        color = color,
        progressValue = progress,
        onTimeChange = vm::startTimer,
        onTimeCancel = vm::cancelTimer,
        onTimePause = vm::pauseTimer,
        timerState = vm.tickingState
    )
}
