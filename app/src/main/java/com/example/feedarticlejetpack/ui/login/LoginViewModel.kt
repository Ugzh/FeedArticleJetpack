package com.example.feedarticlejetpack.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.network.ApiService
import com.example.feedarticlejetpack.network.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: Prefs
): ViewModel() {

    private var _userMessage = MutableLiveData<Int>()
    val userMessage : LiveData<Int>
        get() = _userMessage

    private var _isLogged = MutableLiveData(false)
    val isLogged : LiveData<Boolean>
        get() = _isLogged

    fun logUser(login: String, password: String) {
        val trimLogin = login.trim()
        val trimPassword = password.trim()
        if (trimLogin.isNotEmpty() && trimPassword.isNotEmpty()){
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO){
                    db.logUser(trimLogin, trimPassword)
                }

                val body = response?.body()

                when{
                    response == null ->
                        _userMessage.value = R.string.no_response_database

                    body == null -> _userMessage.value = R.string.error_from_database

                    response.isSuccessful -> {
                        when(body.status){
                            5 -> _userMessage.value = R.string.internal_problem
                            1 -> {
                                myPrefs.token = body.token
                                myPrefs.userId = body.idUser
                                _isLogged.value = true
                            }
                            0,-1 -> _userMessage.value = R.string.wrong_informations
                            else -> return@launch
                        }
                    }
                }
            }
        } else
            _userMessage.value = R.string.fill_fields
    }
}