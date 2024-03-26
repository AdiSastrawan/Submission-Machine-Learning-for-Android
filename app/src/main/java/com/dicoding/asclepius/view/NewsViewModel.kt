package com.dicoding.asclepius.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.ApiConfig
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.HealthArticleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private var mArticlesItem = MutableLiveData<List<ArticlesItem>>()
    val articlesItem : LiveData<List<ArticlesItem>> = mArticlesItem

    private var mIsLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = mIsLoading
    init {
        getNews()
    }
    private fun getNews() {
        mIsLoading.value = true
        val client = ApiConfig.getApiService().getNews(q = "Cancer")
        client.enqueue(object : Callback<HealthArticleResponse> {
            override fun onResponse(
                call: Call<HealthArticleResponse>,
                response: Response<HealthArticleResponse>
            ) {
                Log.d("AppRepository",response.body().toString())
                if(response.isSuccessful){
                    mIsLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        mArticlesItem.value = responseBody.articles
                    }
                }
            }

            override fun onFailure(call: Call<HealthArticleResponse>, t: Throwable) {
                Log.d("AppRepository","onFailure : ${t.message}")
            }

        })
    }

}