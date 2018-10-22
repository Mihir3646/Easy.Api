package com.hlabexamples.easyapi.kotlin.data.webservice

import com.hlabexmaples.easyapi.BuildConfig
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Webservice utility class.
 */

object ApiFactory {

  private var wsCallback: ApiRepository? = null

  /**
   * Static method to to get api client instance
   *
   * @return ApiCallback instance
   */
  val instance: ApiRepository?
    get() {

      try {
        if (wsCallback == null) {

          val httpClient = OkHttpClient.Builder()
          httpClient.connectTimeout(Constants.WS_CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
          httpClient.readTimeout(Constants.WS_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)

          if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
          }

          val client = Retrofit.Builder()
              .baseUrl(Constants.WS_BASE_URL)
              .client(httpClient.build())
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .addConverterFactory(GsonConverterFactory.create())
              .build()
          wsCallback = client.create(ApiRepository::class.java)
        }

      } catch (e: Exception) {
        e.printStackTrace()
      }

      return wsCallback
    }

  fun getFileMultiPart(
    partName: String,
    file: File
  ): MultipartBody.Part {

    // create RequestBody instance from file
    val requestFile = RequestBody.create(MediaType.parse("image/*"), file)

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
  }

  fun resetClient() {
    wsCallback = null
  }

}