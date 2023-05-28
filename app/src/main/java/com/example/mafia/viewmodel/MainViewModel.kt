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
package com.example.mafia.voting.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mafia.elements.Utility
import com.example.mafia.elements.Utility.formatTime

class MainViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    private val _time = MutableLiveData(Utility.TIME_COUNTDOWN.formatTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(1.00F)
    val progress: LiveData<Float> = _progress

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _votable = MutableLiveData(true)
    val votable: LiveData<Boolean> = _votable

    private val _voteCount = MutableLiveData(0)
    val voteCount: LiveData<Int> = _voteCount



//    private val _celebrate = SingleLiveEvent<Boolean>()

    // accessed publicly
//    val celebrate: LiveData<Boolean> get() = _celebrate

    fun handleCountDownTimer() {
        if (isPlaying.value == true) {
            pauseTimer()
//            _celebrate.postValue(false)
        } else {
            startTimer()
        }
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        handleTimerValues(false, Utility.TIME_COUNTDOWN.formatTime(), 1.0F, false)
    }

    fun startTimer() {

        _isPlaying.value = true
        countDownTimer = object : CountDownTimer(Utility.TIME_COUNTDOWN, 1000) {

            override fun onTick(millisRemaining: Long) {
                val progressValue = millisRemaining.toFloat() / Utility.TIME_COUNTDOWN
                handleTimerValues(true, millisRemaining.formatTime(), progressValue, false)
//                _celebrate.postValue(false)
            }

            override fun onFinish() {
//                pauseTimer()
                handleReadyButton()
//                _celebrate.postValue(true)
            }
        }.start()
    }

    private fun handleTimerValues(isPlaying: Boolean, text: String, progress: Float, celebrate: Boolean) {
        _isPlaying.value = isPlaying
        _time.value = text
        _progress.value = progress
//        _celebrate.postValue(celebrate)
    }

    fun handleReadyButton(){
        handleVotingValues(false)
    }
    fun handleVoting(){
        _voteCount.value = 0
        for(player in Utility.playerList){
            if (player.voteCounter > 0){
                handleVotingValues(false)
            }
            _voteCount.value = _voteCount.value!! + player.voteCounter
        }
    }

    private fun handleVotingValues(votable: Boolean){
        _votable.value = votable
    }


}
