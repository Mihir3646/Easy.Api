@file:Suppress("UNCHECKED_CAST")

package com.hlabexamples.easyapi.kotlin.data.easyapi.main

import android.content.Context
import com.hlabexmaples.easyapi.data.easyapi.util.LoaderInterface
import com.hlabexmaples.easyapi.data.easyapi.util.NetworkUtils
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by H.T. on 05/01/18.
 */

class EasyApiRx<T : Base<*>>(private val context: Context) {
    private var loaderInterface: LoaderInterface? = null
    private var call: Observable<T>? = null

    val source: Observable<T>?
        get() = NetworkUtils.isNetworkAvailable(context).filter({ available ->
            if (!available)
                loaderInterface!!.showNoInternet()
            available
        }).switchMap({ available ->
            if (available)
                return@switchMap call!!.compose<T>(applyObservableAsync<T>())
            Observable.empty<T>()
        })

    fun setLoaderInterface(loaderInterface: LoaderInterface): EasyApiRx<T> {
        this.loaderInterface = loaderInterface
        return this
    }

    fun initCall(call: Observable<T>): EasyApiRx<T> {
        this.call = call
        return this
    }

    /**
     * Default Executor
     */
    fun execute(responseHandler:  (response: T, isSuccess: Boolean, successMessage: String) -> Unit/*responseHandler: ResponseHandler<T>*/): DisposableObserver<T>? {
        val disposable = getDisposableObserver(responseHandler)

        if (source != null) {
            if (loaderInterface != null)
                loaderInterface!!.showLoading()

            source!!.subscribe(disposable)
            return disposable
        }
        return null

    }

    private fun getDisposableObserver(responseHandler:  (response: T, isSuccess: Boolean, successMessage: String) -> Unit/*responseHandler: ResponseHandler<T>*/): DisposableObserver<T> {
        return object : DisposableObserver<T>() {
            override fun onNext(responseBody: T?) {
                try {
                    responseBody?.let {
                        //TODO Handle extra data for checking success, message etc from base class
                        responseHandler.invoke(it,
                                /*((T) responseBody.getData()).isSuccess()*/ true,
                                it.message.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    NetworkUtils.showApiError(context)
                }

            }

            override fun onError(e: Throwable) {
                if (loaderInterface != null) {
                    loaderInterface!!.showError(NetworkUtils.handleErrorResponse(e)!!)
                }
            }

            override fun onComplete() {
                if (loaderInterface != null)
                    loaderInterface!!.hideLoading()
            }
        }
    }


    /**
     * Generate reusable observable source object.
     *
     * @return ObservableTransformer
     */
    private fun <F> applyObservableAsync(): ObservableTransformer<F, F> {
        return ObservableTransformer { observable -> observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()) }
    }
}

/**
 * Extension function : Execute with Consumer
 */
fun <T : Base<*>> EasyApiRx<T>.executeWith(successConsumer: Consumer<T>, failureConsumer: Consumer<Throwable>) {
    source?.subscribe(successConsumer, failureConsumer)
}

/**
 * Extension function : Execute with custom observer
 */
fun <T : Base<*>> EasyApiRx<T>.executeWith(observer: Observer<T>) {
    source?.subscribe(observer)
}
