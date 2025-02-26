package com.example.feedarticlejetpack.ui.detailArticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlejetpack.R
import com.example.feedarticlejetpack.network.ApiService
import com.example.feedarticlejetpack.network.Prefs
import com.example.feedarticlejetpack.network.dtos.ArticleDto
import com.example.feedarticlejetpack.network.dtos.NewArticleDto
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

    private var _userMessage = MutableLiveData<Int>()
    val userMessage : LiveData<Int>
        get() = _userMessage

    private var _isFavorite = MutableLiveData(false)
    val isFavorite : LiveData<Boolean>
        get() = _isFavorite

    fun setIsFavorite(favInt: Int)  {
        _isFavorite.value = favInt != 0
    }
    fun toggleFavorite(idArticle: Long){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                db.toggleFavorite(idArticle, myPrefs.token!!)
            }

            val body = response?.body()

            when{
                response == null ->  R.string.no_response_database
                body == null -> R.string.error_from_database
                response.isSuccessful -> {
                    when(body.status){
                        1 -> {
                            _isFavorite.value = !_isFavorite.value!!
                            if(_isFavorite.value!!)
                                R.string.add_to_favorite
                            else
                                R.string.remove_to_favorite

                        }
                        0, -1 ->  R.string.status_not_changed
                        -5 ->  R.string.unauthorized
                        else -> return@launch
                    }
                }
                else -> return@launch
            }.let {
                _userMessage.value = it
            }
        }
    }
}