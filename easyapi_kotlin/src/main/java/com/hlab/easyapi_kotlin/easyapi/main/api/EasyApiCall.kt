package com.hlab.easyapi_kotlin.easyapi.main.api

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import retrofit2.Call

interface EasyApiCall<T> {
  fun initCall(call: Call<T>): EasyApi<T>? {
    return null
  }

  fun initCall(call: Observable<T>): EasyApiRx<T>? {
    return null
  }

  fun executeWith(observer: DisposableObserver<T>) {}

  fun executeWith(
    successConsumer: Consumer<T>,
    failureConsumer: Consumer<Throwable>
  ) {
  }

  fun execute(
    showErrorMessages: Boolean,
    responseHandler: (response: T?, isError: Boolean) -> Unit/*responseHandler: ResponseHandler<T>*/
  )

  fun dispose()
}
