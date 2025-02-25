package com.example.feedarticlejetpack.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.network.ApiService
import com.example.feedarticlejetpack.network.Prefs
import com.example.feedarticlejetpack.network.dtos.ArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val myPrefs: Prefs,
    private val db: ApiService
): ViewModel() {

    private var _articlesListLiveData = MutableLiveData<List<ArticleDto>>()

    private var _articlesFilteredListLiveData = MutableLiveData<List<ArticleDto>>()
    val articlesFilteredListLiveData
        get() = _articlesFilteredListLiveData

    private var _userMessage = MutableLiveData<Int>()
    val userMessage
        get() = _userMessage

    private var _isLogout = MutableLiveData(false)
    val isLogout
        get() = _isLogout


    init {
        getAllArticles()
    }
    fun disconnectUser(){
        myPrefs.token = null
        myPrefs.userId = 0
        _isLogout.value = true
    }

    private fun getAllArticles(){
        myPrefs.token?.let {
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO){
                    db.getAllArticles(token = it)
                }

                val body = response?.body()

                when{
                    response == null -> _userMessage.value = R.string.no_response_database
                    body == null -> _userMessage.value = R.string.error_from_database
                    response.isSuccessful -> {
                        when(body.status){
                            "ok" -> {
                                _articlesListLiveData.value = body.articles
                                _articlesFilteredListLiveData.value = body.articles
                            }
                            "unauthorized" -> {
                                _userMessage.value = R.string.unauthorized
                                _isLogout.value = true
                            }
                            else -> {
                                _userMessage.value = R.string.error_from_database_redirection
                                _isLogout.value = true
                            }
                        }
                    }
                }
            }
        }
    }

    fun getFilteredListArticles(idButton: Int){
        when(idButton){
            R.id.rb_home_sport -> 1
            R.id.rb_home_manga -> 2
            R.id.rb_home_various -> 3
            else -> 0
        }.let {cat ->
            _articlesFilteredListLiveData.value  = if (cat != 0)
                _articlesListLiveData.value?.filter { it.category == cat }
            else
                _articlesListLiveData.value?.sortedByDescending { it.createdAt }
        }


    }
}