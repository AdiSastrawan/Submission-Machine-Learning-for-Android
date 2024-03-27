package com.dicoding.asclepius.view

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.ApiConfig
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.HealthArticleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(private val application: Application) : ViewModel() {

    private var mArticlesItem = MutableLiveData<List<ArticlesItem>>()
    val articlesItem : LiveData<List<ArticlesItem>> = mArticlesItem
    private var mIsLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = mIsLoading
    init {
        getNews(DEFAULT_QUERY)
    }
    fun getNews(query:String) {
        mIsLoading.value = true
        val client = ApiConfig.getApiService().getNews(query)
        client.enqueue(object : Callback<HealthArticleResponse> {
            override fun onResponse(
                call: Call<HealthArticleResponse>,
                response: Response<HealthArticleResponse>
            ) {
                if(response.isSuccessful){
                    mIsLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        mArticlesItem.value = responseBody.articles
                    }
                }
            }

            override fun onFailure(call: Call<HealthArticleResponse>, t: Throwable) {
                mIsLoading.value = false
                showToast(t.message.toString())
                Log.d("AppRepository","onFailure : ${t.message}")
            }

        })
    }

    private fun showToast(message : String){
        Toast.makeText(application.applicationContext,message,Toast.LENGTH_LONG).show()
    }
    companion object{
        const val DEFAULT_QUERY = "cancer"
    }

}