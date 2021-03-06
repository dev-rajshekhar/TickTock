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

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterVm : ViewModel() {

    val _time = MutableLiveData(30L)
    val time: LiveData<Long> = _time
    private lateinit var timer: CountDownTimer
    val tickingState: MutableState<TimerState> = mutableStateOf(TimerState.IDLE)
    fun startTimer(
        totalTime: Int
    ) {
        tickingState.value = TimerState.TICKING
        timer = object : CountDownTimer(totalTime * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _time.value = millisUntilFinished / 1000
            }
            override fun onFinish() {
                tickingState.value = TimerState.IDLE
            }
        }
        timer.start()
    }

    fun cancelTimer() {
        tickingState.value = TimerState.IDLE
        if (::timer.isInitialized) {
            timer.cancel()
        }
        _time.value = 30L
    }

    fun pauseTimer() {
        tickingState.value = TimerState.PAUSED
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }
}
