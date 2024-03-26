package com.dicoding.asclepius.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getNews(
        @Query("q") q : String,
        @Query("category") category : String = "health",
        @Query("apiKey") apiKey : String ="b50e91cceeb74662b1fe3e059d7ce544"
    ) : Call<HealthArticleResponse>

}