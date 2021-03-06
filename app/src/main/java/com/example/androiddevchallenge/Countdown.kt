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

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.BlackColor
import com.example.androiddevchallenge.ui.theme.GreyColor

@Composable
fun Countdown(
    percentage: Float,
    color: Color,
    opacity: Float,
    progressValue: Long,
    onTimeChange: (Int) -> Unit,
    onTimeCancel: () -> Unit,
    onTimePause: () -> Unit,
    timerState: MutableState<TimerState>
) {
    println(" ===progressValue$progressValue")

    Scaffold(
        bottomBar = {
            BottomBar(color)
        },
        topBar = {
            Row(

                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = BlackColor).padding(5.dp)
                    .animateContentSize()
                    .height(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "",
                    tint = GreyColor,
                    modifier = Modifier
                        .size(40.dp)
                        .animateContentSize()

                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_bas_help),
                    contentDescription = "",
                    tint = GreyColor,
                    modifier = Modifier
                        .size(40.dp)
                        .animateContentSize()
                )
            }
        },
        content = {
            ClockUi(
                progressValue,
                onTimeChange,
                color,
                percentage,
                opacity,
                onTimeCancel,
                onTimePause, timerState.value
            )
        }
    )
}

@Composable
fun ClockUi(
    progressValue: Long,
    onTimeChange: (Int) -> Unit,
    color: Color,
    percentage: Float,
    opacity: Float,
    onTimeCancel: () -> Unit,
    onTimePause: () -> Unit,
    timerState: TimerState,

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = BlackColor)
            .fillMaxHeight()
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.6f)
                .size(250.dp)
        ) {
            val (progressBar1, progressBar2, text) = createRefs()
            CircularProgressIndicator(
                modifier = Modifier
                    .size(200.dp)
                    .constrainAs(progressBar1) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                progress = 1f,
                strokeWidth = 3.dp,
                color = color.copy(alpha = opacity)
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(180.dp)
                    .constrainAs(progressBar2) {
                        top.linkTo(progressBar1.top)
                        bottom.linkTo(progressBar1.bottom)
                        start.linkTo(progressBar1.start)
                        end.linkTo(progressBar1.end)
                    }
                    .animateContentSize(),
                progress = percentage,
                strokeWidth = 6.dp,
                color = color

            )

            Text(
                "$progressValue",
                modifier = Modifier

                    .constrainAs(text) {
                        top.linkTo(progressBar2.top)
                        bottom.linkTo(progressBar2.bottom)
                        start.linkTo(progressBar2.start)
                        end.linkTo(progressBar2.end)
                    },
                style = MaterialTheme.typography.h3.copy(color = Color.White)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {

            IconButton(
                onClick = {
                    onTimeCancel()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier
                        .size(48.dp)

                )
            }

            IconButton(
                onClick = {
                    when (timerState) {
                        TimerState.PAUSED -> {
                            onTimeChange(progressValue.toInt())
                        }
                        TimerState.TICKING -> {
                            onTimePause()
                        }
                        TimerState.CANCELLED -> {
                            onTimeCancel()
                        }
                        TimerState.IDLE -> {
                            onTimeChange(30)
                        }
                    }
                }
            ) {
                Crossfade(targetState = timerState) {
                    if (timerState == TimerState.TICKING) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause),
                            contentDescription = "",
                            tint = color,
                            modifier = Modifier
                                .size(48.dp)

                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_button),
                            contentDescription = "",
                            tint = color,
                            modifier = Modifier
                                .size(48.dp)

                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    onTimeCancel()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_reset),
                    contentDescription = "",
                    tint = color,
                    modifier = Modifier
                        .size(48.dp)

                )
            }
        }
    }
}

@Composable
fun BottomBar(color: Color) {
    Column {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = color)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "",
                tint = GreyColor,
                modifier = Modifier
                    .size(48.dp)
                    .padding(5.dp)

            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(color = color)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_alarm),
                contentDescription = "",
                tint = GreyColor,
                modifier = Modifier
                    .size(48.dp)
                    .padding(5.dp)

            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(color = color)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_timer),
                contentDescription = "",
                tint = color,
                modifier = Modifier
                    .size(48.dp)
                    .padding(5.dp)

            )
        }
    }
}
