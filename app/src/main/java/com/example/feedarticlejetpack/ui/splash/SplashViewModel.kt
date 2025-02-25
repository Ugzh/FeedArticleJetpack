package com.example.feedarticlejetpack.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel(){

    private var _isLoading = MutableLiveData(true)
    val __isLoading
        get () = _isLoading

}