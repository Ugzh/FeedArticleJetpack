package com.example.feedarticlejetpack.ui.detailArticle

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
class DetailArticleViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: Prefs
) : ViewModel() {
    private var _articleLiveData = MutableLiveData<ArticleDto>()
    val articleLiveData
        get() = _articleLiveData

    private var _userMessage = MutableLiveData<Int>()
    val userMessage
        get() = _userMessage


    fun getArticleDetail(idArticle: Long){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                db.getArticle(myPrefs.token!!, idArticle)
            }

            val body = response?.body()

            when{
                response == null -> _userMessage.value = R.string.no_response_database
                body == null -> _userMessage.value = R.string.error_from_database
                response.isSuccessful -> {
                    when(body.status){
                        "ok" -> {
                            _articleLiveData.value = body.article
                        }
                        "unauthorized" -> {
                            _userMessage.value = R.string.unauthorized
                        }
                        else -> {
                            _userMessage.value = R.string.error_from_database_redirection
                        }
                    }
                }
            }
        }
    }
}