package com.example.epoxypaging3.network

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResponseAdapter<T: Any>(
  private val successType: Type,
) : CallAdapter<T, Call<ApiResponse<T>>> {
  override fun responseType(): Type = successType

  override fun adapt(call: Call<T>): Call<ApiResponse<T>> {
    return ApiResponseCall(call)
  }
}