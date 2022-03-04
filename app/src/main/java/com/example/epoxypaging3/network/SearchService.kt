package com.example.epoxypaging3.network

import com.example.epoxypaging3.model.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

  @GET("/search/users")
  suspend fun getSearchUsers(
    @Query("q") query: String,
    @Query("sort") sort: String? = null,
    @Query("order") order: String? = null,  //Default: desc
    @Query("per_page") perPage: Int? = null, //Default: 30
    @Query("page") page: Int = 1,
  ): ApiResponse<SearchUserResponse>
}