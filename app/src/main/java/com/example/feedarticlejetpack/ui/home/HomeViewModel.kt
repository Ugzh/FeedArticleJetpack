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
    val articlesListLiveData : LiveData<List<ArticleDto>> get() = _articlesListLiveData

    private var _navDirectionLiveData = MutableLiveData<NavDirections?>()
    val navDirectionLiveData : LiveData<NavDirections?> get() = _navDirectionLiveData

    private var _userMessage = MutableLiveData<Int>()
    val userMessage : LiveData<Int> get() = _userMessage

    private var _isLogout = MutableLiveData(false)
    val isLogout : LiveData<Boolean> get() = _isLogout

    private var _isFavoriteChecked = MutableLiveData(false)

    val isFavoriteChecked : LiveData<Boolean> get() = _isFavoriteChecked

    private var numCat = 0

    init {
        getAllArticles()
    }
    fun disconnectUser(){
        myPrefs.token = null
        myPrefs.userId = 0
        _isLogout.postValue(true)
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
                                when(_isFavoriteChecked.value!!){
                                    true -> setFilteredCategoryAndLikedList(numCat, body.articles)
                                    false -> setFilteredCategoryList(numCat, body.articles)
                                }.let {
                                    _articlesListLiveData.value = it
                                }
                            }
                            "unauthorized" -> {
                                _userMessage.value = R.string.unauthorized
                                disconnectUser()
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

    private fun setFilteredCategoryAndLikedList(idCat: Int, articleList: List<ArticleDto>): List<ArticleDto>{
        when(idCat){
            1, 2, 3 -> articleList
                .filter { it.category == idCat }
                .filter { it.isFav == 1 }
            else -> articleList.filter { it.isFav == 1 }
        }.let {
            return it
        }
    }

    private fun setFilteredCategoryList(idCat: Int, articleList: List<ArticleDto>): List<ArticleDto>{
        when(idCat) {
            1, 2, 3 -> articleList.filter { it.category == idCat }
            else -> articleList
        }.let {
            return it
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

    fun setFilter(idButton: Int){
        numCat = when(idButton){
            R.id.rb_home_sport -> 1
            R.id.rb_home_manga -> 2
            R.id.rb_home_various -> 3
            else -> 0
        }
        getAllArticles()
    }

    fun toggleFavoriteChecked(){
        _isFavoriteChecked.value = !_isFavoriteChecked.value!!
    }

}