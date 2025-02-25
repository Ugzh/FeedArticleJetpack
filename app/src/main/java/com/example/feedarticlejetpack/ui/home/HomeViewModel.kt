package com.example.feedarticlejetpack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
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
    val articlesFilteredListLiveData : LiveData<List<ArticleDto>>
        get() = _articlesFilteredListLiveData

    private var _navDirectionLiveData = MutableLiveData<NavDirections?>()
    val navDirectionLiveData : LiveData<NavDirections?>
        get() = _navDirectionLiveData

    private var _userMessage = MutableLiveData<Int>()
    val userMessage : LiveData<Int>
        get() = _userMessage

    private var _isLogout = MutableLiveData(false)
    val isLogout : LiveData<Boolean>
        get() = _isLogout

    private var _isFavoriteChecked = MutableLiveData(false)

    val isFavoriteChecked : LiveData<Boolean>
        get() = _isFavoriteChecked

    init {
        getAllArticles()
    }
    fun disconnectUser(){
        myPrefs.token = null
        myPrefs.userId = 0
        _isLogout.value = true
    }
    fun getAllArticles(){
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

    fun openEditOrDetailFragment(idArticle: Long){
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
                            if(body.article.idU == myPrefs.userId)
                                _navDirectionLiveData.value =
                                    HomeFragmentDirections.
                                    actionHomeFragmentToEditArticleFragment(body.article)
                            else
                                _navDirectionLiveData.value =
                                    HomeFragmentDirections.
                                    actionHomeFragmentToDetailArticleFragment(body.article)
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

    fun resetNavDirectionLiveData(){
        _navDirectionLiveData.value = null
    }

    fun getFilteredListArticles(idButton: Int){
        when(idButton){
            R.id.rb_home_sport -> 1
            R.id.rb_home_manga -> 2
            R.id.rb_home_various -> 3
            else -> 0
        }.let{ cat ->
            when(_isFavoriteChecked.value!!){
                true -> when(cat){
                    1, 2, 3 -> _articlesListLiveData.value!!
                        .filter { it.category == cat }
                        .filter { it.isFav == 1 }
                    else -> _articlesListLiveData.value?.filter { it.isFav == 1 }
                    }
                false -> when(cat){
                    1, 2, 3 -> _articlesListLiveData.value?.filter { it.category == cat }
                    else -> _articlesListLiveData.value?.sortedByDescending { it.createdAt }
                }
            }.also {
                _articlesFilteredListLiveData.value = it
            }
        }
    }

    fun toggleFavoriteList(){
        _isFavoriteChecked.value = !_isFavoriteChecked.value!!
    }
}