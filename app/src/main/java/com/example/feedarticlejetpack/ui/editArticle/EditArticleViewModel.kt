package com.example.feedarticlejetpack.ui.editArticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.network.ApiService
import com.example.feedarticlejetpack.network.Prefs
import com.example.feedarticlejetpack.network.dtos.ArticleDto
import com.example.feedarticlejetpack.network.dtos.UpdateArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: Prefs
): ViewModel() {

    private var _userMessage = MutableLiveData<Int>()
    val userMessage : LiveData<Int>
        get() = _userMessage

    private var _isUpdatedOrDelete = MutableLiveData(false)
    val isUpdatedOrDelete : LiveData<Boolean>
        get() = _isUpdatedOrDelete

    fun deleteArticle(idArticle: Long){
        viewModelScope.launch{
            val response = withContext(Dispatchers.IO){
                db.deleteArticle(idArticle, myPrefs.token!!)
            }

            val body = response?.body()

            when{
                response == null -> _userMessage.value = R.string.no_response_database
                body == null -> _userMessage.value = R.string.error_from_database
                response.isSuccessful -> {
                    when(body.status){
                        1 -> {
                            _userMessage.value = R.string.article_deleted
                            _isUpdatedOrDelete.value = true
                        }
                        0, -1, -2 -> _userMessage.value = R.string.article_not_deleted
                        -5 -> _userMessage.value = R.string.unauthorized
                        else -> return@launch
                    }
                }
            }
        }
    }
    fun updateArticle(idArticle: Long, title: String, desc: String, image: String, idButton: Int){
        val trimTitle = title.trim()
        val trimDesc = desc.trim()
        val trimImage = image.trim()
        val cat = when(idButton){
            R.id.rb_editArticle_sport -> 1
            R.id.rb_editArticle_manga -> 2
            R.id.rb_editArticle_various -> 3
            else -> 0
        }

        if(
            trimTitle.isNotEmpty()
            && trimDesc.isNotEmpty()
            && trimImage.isNotEmpty()
            && cat != 0
            ) {
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO){
                    db.updateArticle(
                        idArticle, myPrefs.token!!,
                        UpdateArticleDto(idArticle, trimTitle, trimDesc, trimImage, cat))
                }

                val body = response?.body()

                when{
                    response == null -> _userMessage.value = R.string.no_response_database
                    body == null -> _userMessage.value = R.string.error_from_database
                    response.isSuccessful -> {
                        when(body.status){
                            1 -> {
                                _userMessage.value = R.string.article_updated
                                _isUpdatedOrDelete.value = true
                            }
                            0 -> _userMessage.value = R.string.article_content_same
                            -1, -2 -> _userMessage.value = R.string.article_not_updated
                            -5 -> _userMessage.value = R.string.unauthorized
                            else -> return@launch
                        }
                    }
                }
            }
        } else
            _userMessage.value = R.string.fill_fields

    }

}