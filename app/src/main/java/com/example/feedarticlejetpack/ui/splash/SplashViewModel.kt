package com.example.feedarticlejetpack.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feedarticlejetpack.network.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor( myPrefs: Prefs): ViewModel(){

    private var _isAlreadyConnected = MutableLiveData(false)
    val isAlreadyConnected
        get () = _isAlreadyConnected
    init {
        myPrefs.token?.let {
            _isAlreadyConnected.value = true
        }
    }
}