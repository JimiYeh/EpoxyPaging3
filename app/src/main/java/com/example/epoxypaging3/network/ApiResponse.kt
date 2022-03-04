package com.example.epoxypaging3.network

import okhttp3.ResponseBody

sealed class ApiResponse<out T : Any> {
  /**
   * Success response with body
   */
  data class ApiSuccess<T : Any>(val data: T) : ApiResponse<T>()

  /**
   * Failure response with body
   */
  data class ApiFail(val errorBody: ResponseBody?, val code: Int) : ApiResponse<Nothing>()


  /**
   * For example, json parsing error or timeout exception
   */
  data class UnknownError(val error: Throwable?) : ApiResponse<Nothing>()
}