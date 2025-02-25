package com.example.feedarticlejetpack.network

import android.content.Context
import android.content.SharedPreferences
import com.example.feedarticlejetpack.utils.TOKEN
import com.example.feedarticlejetpack.utils.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class Prefs @Inject constructor(private val sharedPreferences: SharedPreferences){
    var userId : Long
        get() = sharedPreferences.getLong(USER_ID, 0L)
        set(value) = sharedPreferences.edit().putLong(USER_ID,value).apply()


    var token: String?
        get() = sharedPreferences.getString(TOKEN,null)
        set(value) = sharedPreferences.edit().putString(TOKEN, value).apply()

}