package com.hlab.easyapi_kotlin.easyapi.main.api

import com.hlab.easyapi_kotlin.R
import com.hlab.easyapi_kotlin.easyapi.util.NetworkUtils
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by H.T. on 05/01/18.
 */

class EasyApiRx<T> internal constructor(config: Config) : EasyApiBase(), EasyApiCall<T> {

  private var call: Observable<T>? = null
  private var disposable: DisposableObserver<T>? = null

  private val source: Observable<T>?
    get() = NetworkUtils.isNetworkAvailable(config.getmContext())
        .filter { available ->
          if (!available) {
            showNoInternet()
          }
          available
        }
        .switchMap { available ->
          if (available!!) {
            call?.compose<T>(applyObservableAsync<T>())
          } else
            Observable.empty<T>()
        }

  init {
    this.config = config
  }

  override fun initCall(call: Observable<T>): EasyApiRx<T>? {
    this.call = call
    return this
  }

  override fun execute(
    showErrorMessages: Boolean,
    responseHandler: (response: T?, isError: Boolean) -> Unit/*responseHandler: ResponseHandler<T>*/
  ) {
    val disposable = getDisposableObserver(responseHandler, showErrorMessages)

    val source = source

    if (source != null) {
      showProgress()

      source.subscribe(disposable)
      this.disposable = disposable
    }
  }

  override fun executeWith(observer: DisposableObserver<T>) {
    val source = source

    source?.subscribe(observer)
  }

  override fun executeWith(
    successConsumer: Consumer<T>,
    failureConsumer: Consumer<Throwable>
  ) {
    val source = source

    source?.subscribe(successConsumer, failureConsumer)
  }

  private fun getDisposableObserver(
    responseHandler: (response: T?, isError: Boolean) -> Unit/*responseHandler: ResponseHandler<T>*/,
    showErrorMessages: Boolean
  ): DisposableObserver<T> {
    return object : DisposableObserver<T>() {
      override fun onNext(responseBody: T?) {
        try {
          if (responseBody != null) {
            responseHandler.invoke(responseBody, true)
          } else {
            responseHandler.invoke(null, false)
            if (showErrorMessages) {
              showError(config.getmContext().getString(R.string.alert_message_error))
            }
          }
        } catch (e: Exception) {
          e.printStackTrace()
          responseHandler.invoke(null, false)
          if (showErrorMessages) {
            showError(config.getmContext().getString(R.string.alert_message_error))
          }
        }

      }

      override fun onError(e: Throwable) {
        responseHandler.invoke(null, false)
        if (showErrorMessages) {
          showError(NetworkUtils.handleErrorResponse(e))
        }
      }

      override fun onComplete() {
        hideProgress()
      }
    }
  }

  override fun dispose() {
    disposable?.let {
      if (!it.isDisposed)
        it.dispose()
    }
  }

  /**
   * Generate reusable observable source object.
   *
   * @return ObservableTransformer
   */
  private fun <F> applyObservableAsync(): ObservableTransformer<F, F> {
    return ObservableTransformer { observable ->
      observable.subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())
    }
  }
}
