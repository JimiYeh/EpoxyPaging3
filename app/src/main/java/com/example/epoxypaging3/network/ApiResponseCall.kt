package com.example.epoxypaging3.network

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiResponseCall<T : Any>(
  private val delegate: Call<T>
) : Call<ApiResponse<T>> {
  override fun clone(): Call<ApiResponse<T>> = ApiResponseCall(delegate.clone())

  override fun execute(): Response<ApiResponse<T>> {
    // 都使用 coroutine  應該不可能出現需要 執行 block 的 execute
    throw UnsupportedOperationException("ApiResponse doesn't support execute")
  }

  override fun enqueue(callback: Callback<ApiResponse<T>>) {
    return delegate.enqueue(object : Callback<T> {
      override fun onResponse(call: Call<T>, response: Response<T>) {
        val body = response.body()
        val code = response.code()
        val error = response.errorBody()

        if (response.isSuccessful) {
          if (body != null) {
            callback.onResponse(
              this@ApiResponseCall,
              Response.success(ApiResponse.ApiSuccess(body))
            )
          } else {
            // Response is successful but the body is null
            callback.onResponse(
              this@ApiResponseCall,
              Response.success(ApiResponse.ApiFail(null, code))
            )
          }
        } else {
          callback.onResponse(
            this@ApiResponseCall,
            Response.success(ApiResponse.ApiFail(error, code))
          )
        }
      }

      override fun onFailure(call: Call<T>, t: Throwable) {
        TODO("Not yet implemented")
      }

    })
  }

  override fun isExecuted(): Boolean = delegate.isExecuted

  override fun cancel() = delegate.cancel()

  override fun isCanceled(): Boolean = delegate.isCanceled

  override fun request(): Request = delegate.request()

  override fun timeout(): Timeout = delegate.timeout()

}