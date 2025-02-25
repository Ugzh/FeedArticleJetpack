package com.example.feedarticlejetpack.ui.addArticle

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.network.ApiService
import com.example.feedarticlejetpack.network.Prefs
import com.example.feedarticlejetpack.network.dtos.NewArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddArticleViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: Prefs
) : ViewModel(){

    private var _userMessage = MutableLiveData<Int>()
    val userMessage
        get() = _userMessage

    private var _isArticleCreated = MutableLiveData(false)
    val isArticleCreated
        get() = _isArticleCreated

    fun addArticle(title: String, desc: String, image: String, idButton: Int){
        val trimTitle = title.trim()
        val trimDesc = desc.trim()
        val trimImage = image.trim()
        val cat = when(idButton){
            R.id.rb_addArticle_sport -> 1
            R.id.rb_addArticle_manga -> 2
            R.id.rb_addArticle_divers -> 3
            else -> 0
        }

        if(
            trimTitle.isNotEmpty()
            && trimDesc.isNotEmpty()
            && trimImage.isNotEmpty()
            && cat != 0){
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO){
                    db.addArticle(
                        myPrefs.token!!,
                        NewArticleDto(myPrefs.userId,trimTitle,trimDesc,trimImage,cat))
                }

                val body = response?.body()

                when{
                    response == null -> _userMessage.value = R.string.no_response_database
                    body == null -> _userMessage.value = R.string.error_from_database
                    response.isSuccessful -> {
                        when(body.status){
                            1 -> {
                                _userMessage.value = R.string.article_created
                                _isArticleCreated.value = true
                            }
                            0, -1 -> _userMessage.value = R.string.article_not_created
                            -5 -> _userMessage.value = R.string.unauthorized
                        }
                    }
                }
            }
        } else
            _userMessage.value = R.string.fill_fields
    }
}