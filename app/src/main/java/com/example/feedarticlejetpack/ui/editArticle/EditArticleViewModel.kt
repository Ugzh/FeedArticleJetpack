package com.example.feedarticlejetpack.ui.editArticle

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
class EditArticleViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: Prefs
): ViewModel() {

    private var _userMessage = MutableLiveData<Int>()
    val userMessage
        get() = _userMessage


}