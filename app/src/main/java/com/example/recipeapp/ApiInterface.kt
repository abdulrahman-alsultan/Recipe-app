package com.example.recipeapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("recipes/")
    fun getData(): Call<List<DataItem>>

    @POST("recipes/")
    fun postData(@Body data: DataItem): Call<Data>

}