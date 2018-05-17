package com.hlabexamples.easyapi.kotlin.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import com.hlabexamples.easyapi.kotlin.data.easyapi.main.EasyApi
import com.hlabexamples.easyapi.kotlin.data.easyapi.main.EasyApiRx
import com.hlabexamples.easyapi.kotlin.data.models.Envelop
import com.hlabexamples.easyapi.kotlin.data.models.User
import com.hlabexamples.easyapi.kotlin.data.webservice.ApiFactory
import com.hlabexmaples.easyapi.data.easyapi.util.LoaderInterface
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by H.T. on 22/02/18.
 */

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val usersLiveData: MediatorLiveData<List<User>>
    private val disposable: CompositeDisposable?

    private var pageNo = 1

    init {

        disposable = CompositeDisposable()
        // initialize live data
        usersLiveData = MediatorLiveData()
        // set by default null
        usersLiveData.value = null

    }

    /**
     * Api call with EasyApi
     * Expose the LiveData Products query so the UI can observe it.
     */
    fun fetchUsers(loaderInterface: LoaderInterface) {
        EasyApi<Envelop<List<User>>>(getApplication())
                .setLoaderInterface(loaderInterface)
                .initCall(ApiFactory.instance!!.fetchUsers(pageNo.toString()))
                .execute { response, isSuccess, _ ->
                    if (isSuccess) {
                        usersLiveData.value = response.data
                    }
                }
    }


    /**
     * Api call with EasyApiRx
     * Expose the LiveData Products query so the UI can observe it.
     */
    fun fetchUsersRx(loaderInterface: LoaderInterface) {
        val d = EasyApiRx<Envelop<List<User>>>(getApplication())
                .initCall(ApiFactory.instance!!.fetchUsersWithRx(pageNo.toString()))
                .setLoaderInterface(loaderInterface)
                .execute { response, isSuccess, _ ->
                    if (isSuccess) {
                        usersLiveData.value = response.data
                    }
                }
        disposable!!.add(d!!)
    }

    /**
     * Test function to reset live data, DON'T do in actual development
     */
    fun resetData() {
        usersLiveData.value = null
    }

    fun setNextPage() {
        pageNo++
    }

    fun onDestroy() {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

//    /*Extension function*/
//    private fun <T : Base<*>> EasyApi<T>.execute() {
//    }
}
