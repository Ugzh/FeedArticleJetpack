package com.example.feedarticlejetpack.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.network.ApiService
import com.example.feedarticlejetpack.network.dtos.RegisterDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val db: ApiService) : ViewModel() {

    private var _userMessage = MutableLiveData<Int>()
    val userMessage
        get() = _userMessage

    private var _isRegister = MutableLiveData(false)
    val isRegister
        get() = _isRegister

    fun registerUser(login: String, password: String, confirmPassword: String){
        val trimLogin = login.trim()
        val trimPassword = password.trim()
        val trimConfirmPassword = confirmPassword.trim()

        if(
            trimLogin.isNotEmpty()
            && trimPassword.isNotEmpty()
            && trimConfirmPassword.isNotEmpty()
            ){
            if(trimPassword == trimConfirmPassword){
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO){
                        db.registerUser(RegisterDto(login,password))
                    }
                    val body = response?.body()

                    when{
                        response == null -> _userMessage.value = R.string.no_response_database
                        body == null -> _userMessage.value = R.string.error_from_database
                        response.isSuccessful -> {
                            when(body.status){
                                5 -> userMessage.value = R.string.login_already_used
                                0,-1 -> userMessage.value = R.string.account_not_created
                                1 -> {
                                    _userMessage.value = R.string.account_created
                                    _isRegister.value = true
                                }
                            }
                        }
                    }
                }
            } else
                _userMessage.value = R.string.password_not_match
        } else
            _userMessage.value = R.string.fill_fields
    }
}