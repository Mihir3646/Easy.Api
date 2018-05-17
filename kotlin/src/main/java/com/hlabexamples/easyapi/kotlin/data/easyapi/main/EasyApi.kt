@file:Suppress("UNCHECKED_CAST")

package com.hlabexamples.easyapi.kotlin.data.easyapi.main

import android.content.Context
import android.view.View
import com.hlabexmaples.easyapi.data.easyapi.util.LoaderInterface
import com.hlabexmaples.easyapi.data.easyapi.util.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by H.T. on 05/01/18.
 */

class EasyApi<T : Base<*>>(private val context: Context) {
    private var loaderInterface: LoaderInterface? = null
    private var call: Call<T>? = null

    fun setLoaderInterface(loaderInterface: LoaderInterface): EasyApi<T> {
        this.loaderInterface = loaderInterface
        return this
    }

    fun initCall(call: Call<T>): EasyApi<T> {
        this.call = call
        return this
    }

    fun execute(responseHandler:  (response: T, isSuccess: Boolean, successMessage: String) -> Unit/*responseHandler: ResponseHandler<T>*/) {
        if (NetworkUtils.isNetworkOn(context)) {
            val callback = object : Callback<T> {

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    loaderInterface?.hideLoading()

                    try {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                //TODO Handle extra data for checking success, message etc from base class
                                responseHandler.invoke(it,
                                        /*((T) responseBody.getData()).isSuccess()*/ true,
                                        it.message.toString())
                            }
                        } else
                            NetworkUtils.showApiError(context)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetworkUtils.showApiError(context)
                    }

                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    loaderInterface?.let {
                        it.hideLoading()
                        it.showError(NetworkUtils.handleErrorResponse(t)!!)
                    }
                }
            }

            loaderInterface?.showLoading()

            call!!.enqueue(callback)
        } else
            loaderInterface?.showNoInternet()
    }

    fun execute(responseHandler:  (response: T) -> Unit){

    }
}
