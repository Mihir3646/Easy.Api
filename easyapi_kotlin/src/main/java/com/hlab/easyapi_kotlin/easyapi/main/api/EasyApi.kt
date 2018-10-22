package com.hlab.easyapi_kotlin.easyapi.main.api

import com.hlab.easyapi_kotlin.R
import com.hlab.easyapi_kotlin.easyapi.util.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by H.T. on 05/01/18.
 */

class EasyApi<T> internal constructor(config: Config) : EasyApiBase(), EasyApiCall<T> {

  private lateinit var call: Call<T>

  init {
    this.config = config
  }

  override fun initCall(call: Call<T>): EasyApi<T>? {
    this.call = call
    return this
  }

  override fun execute(
    showErrorMessages: Boolean,
    responseHandler: (response: T?, isError: Boolean) -> Unit/*responseHandler: ResponseHandler<T>*/
  ) {
    if (NetworkUtils.isNetworkOn(config.getmContext())) {
      val callback = object : Callback<T> {

        override fun onResponse(
          call: Call<T>,
          response: Response<T>
        ) {
          hideProgress()

          try {
            if (response.isSuccessful) {
              val responseBody = response.body()
              if (responseBody != null) {
                //TODO Handle extra data for checking success, message etc from base class
                responseHandler.invoke(responseBody, false)
              } else {
                responseHandler.invoke(null, true)
                if (showErrorMessages) {
                  showError(config.getmContext().getString(R.string.alert_message_error))
                }
              }
            } else {
              responseHandler.invoke(null, true)
              if (showErrorMessages) {
                showError(config.getmContext().getString(R.string.alert_message_error))
              }
            }
          } catch (e: Exception) {
            e.printStackTrace()
            responseHandler.invoke(null, true)
            if (showErrorMessages) {
              showError(config.getmContext().getString(R.string.alert_message_error))
            }
          }

        }

        override fun onFailure(
          call: Call<T>,
          t: Throwable
        ) {
          hideProgress()
          responseHandler.invoke(null, true)
          if (showErrorMessages) {
            showError(config.getmContext().getString(R.string.alert_message_error))
          }
        }
      }

      showProgress()
      call.enqueue(callback)
    } else {
      showNoInternet()
    }
  }

  override fun dispose() {
    if (!call.isCanceled) {
      call.cancel()
    }
  }
}
