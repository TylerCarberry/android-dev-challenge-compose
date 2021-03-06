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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _secondsRemaining = MutableLiveData(0)
    val secondsRemaining: LiveData<Int> = _secondsRemaining

    private val _percent = MutableLiveData(1f)
    val percent: LiveData<Float> = _percent

    private val _hours = MutableLiveData(0)
    val hours: LiveData<Int> = _hours

    private val _minutes = MutableLiveData(0)
    val minutes: LiveData<Int> = _minutes

    private val _seconds = MutableLiveData(0)
    val seconds: LiveData<Int> = _seconds

    fun startTimer() {
        viewModelScope.launch {
            _isRunning.value = true
            Log.d(MainActivity.TAG, "START TIMER: ${hours.value} ${minutes.value} ${seconds.value}")
            val startTime = getEnteredTimeSeconds()
            _secondsRemaining.value = startTime

            delay(1000)
            while (secondsRemaining.value ?: 0 > 0 && _isRunning.value == true) {
                Log.d(MainActivity.TAG, "START TIMER: tick ${secondsRemaining.value}")

                val remaining: Int = (secondsRemaining.value ?: 1) - 1
                _secondsRemaining.value = remaining
                _percent.value = 1 - ((startTime - remaining) / startTime.toFloat())

                _seconds.value = remaining % 60
                _minutes.value = (remaining / 60) % 60
                _hours.value = (remaining / 60 / 60)

                delay(1000)
            }
            _isRunning.value = false
        }
    }

    fun stopTimer() {
        _isRunning.value = false
    }

    private fun getEnteredTimeSeconds(): Int {
        return (hours.value ?: 0) * 60 * 60 +
            (minutes.value ?: 0) * 60 +
            (seconds.value ?: 0)
    }

    fun onDigitPressed(num: Int) {
        Log.d(MainActivity.TAG, "onDigitPressed() called with: num = $num")

        if (_hours.value ?: 0 <= 9) {
            var digits = concatHoursMinSec(hours.value ?: 0, minutes.value ?: 0, seconds.value ?: 0)
            digits = digits.substring(1) + num.toString()
            _hours.value = digits.substring(0, 2).toInt()
            _minutes.value = digits.substring(2, 4).toInt()
            _seconds.value = digits.substring(4, 6).toInt()
        }

        Log.d(MainActivity.TAG, "onDigitPressed: ${hours.value} ${minutes.value} ${seconds.value}")
    }

    private fun concatHoursMinSec(hours: Int, minutes: Int, seconds: Int): String {
        return hours.toString().padStart(2, '0') +
            minutes.toString().padStart(2, '0') +
            seconds.toString().padStart(2, '0')
    }

    fun backspace() {
        var digits = concatHoursMinSec(hours.value ?: 0, minutes.value ?: 0, seconds.value ?: 0)
        digits = "0" + digits.substring(0, digits.length - 1)
        _hours.value = digits.substring(0, 2).toInt()
        _minutes.value = digits.substring(2, 4).toInt()
        _seconds.value = digits.substring(4, 6).toInt()
    }
}
