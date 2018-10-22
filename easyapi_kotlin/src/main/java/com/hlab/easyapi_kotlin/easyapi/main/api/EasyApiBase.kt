package com.hlab.easyapi_kotlin.easyapi.main.api

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.hlab.easyapi_kotlin.easyapi.util.LoaderInterface
import com.hlab.easyapi_kotlin.easyapi.util.STATE

abstract class EasyApiBase {

  lateinit var config: Config

  fun showProgress() {
    if (config.isUsingLiveData)
      config.mProgressLiveData?.postValue(STATE.SHOW_LOADING)
    else
      config.mLoaderInterface?.showLoading()
  }

  fun hideProgress() {
    if (config.isUsingLiveData)
      config.mProgressLiveData?.postValue(STATE.HIDE_LOADING)
    else
      config.mLoaderInterface?.hideLoading()
  }

  fun showNoInternet() {
    if (config.isUsingLiveData)
      config.mProgressLiveData?.postValue(STATE.NO_INTERNET)
    else
      config.mLoaderInterface?.showNoInternet()
  }

  fun showError(error: String) {
    if (config.isUsingLiveData) {
      val state = STATE.ERROR
      state.msg = error
      config.mProgressLiveData?.postValue(state)
    } else
      config.mLoaderInterface?.showError(error)

  }

  class Builder<T>(context: Context) {
    private val config: Config = Config(context)

    fun setLoaderInterface(loaderInterface: LoaderInterface): Builder<T> {
      config.mLoaderInterface = loaderInterface
      return this
    }

    fun attachLoadingLiveData(progressData: MutableLiveData<STATE>): Builder<T> {
      config.isUsingLiveData = true
      config.mProgressLiveData = progressData
      return this
    }

    fun configureWithRx(): Builder<T> {
      config.isRxCall = true
      return this
    }

    fun build(): EasyApiCall<T> {
      return if (!config.isRxCall) {
        EasyApi(config)
      } else {
        EasyApiRx(config)
      }
    }
  }
}
