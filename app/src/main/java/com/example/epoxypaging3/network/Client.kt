package com.example.epoxypaging3.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.github.com/"

val okHttpClient by lazy {
  OkHttpClient.Builder()
    .addInterceptor {
      val requestBuilder = it.request().newBuilder()
        .addHeader("Content-Type", "application/json")
        .addHeader("accept", "application/vnd.github.v3+json")
        .url(it.request().url())
      it.proceed(requestBuilder.build())
    }
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()
}

fun getSearchService(): SearchService =
  Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(ApiResponseAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()
    .create(SearchService::class.java)


