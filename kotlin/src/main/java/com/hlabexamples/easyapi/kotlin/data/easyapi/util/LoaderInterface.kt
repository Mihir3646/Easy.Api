package com.hlabexmaples.easyapi.data.easyapi.util

/**
 * Created by H.T. on 05/01/18.
 */

interface LoaderInterface {
    fun showLoading()

    fun hideLoading()

    fun showNoInternet()

    fun showError(message: String)
}
