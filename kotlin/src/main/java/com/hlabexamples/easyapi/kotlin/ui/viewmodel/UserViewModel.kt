package com.hlabexamples.easyapi.kotlin.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.hlab.easyapi_kotlin.easyapi.main.api.EasyApiBase.Builder
import com.hlab.easyapi_kotlin.easyapi.main.api.EasyApiCall
import com.hlab.easyapi_kotlin.easyapi.util.STATE
import com.hlabexamples.easyapi.kotlin.data.models.Envelop
import com.hlabexamples.easyapi.kotlin.data.models.User
import com.hlabexamples.easyapi.kotlin.data.webservice.ApiFactory

/**
 * Created by H.T. on 22/02/18.
 */

class UserViewModel(application: Application) : AndroidViewModel(application) {
  val usersLiveData: MutableLiveData<List<User>> = MutableLiveData()
  private val loadingStateLiveData: MutableLiveData<STATE> = MutableLiveData()

  private var usersListApi: EasyApiCall<Envelop<List<User>>>? = null
  private var usersListRxApi: EasyApiCall<Envelop<List<User>>>? = null

  private var pageNo = 1

  init {

    // initialize live data
    // set by default null
    usersLiveData.value = null
    usersListApi = Builder<Envelop<List<User>>>(application)
        .attachLoadingLiveData(loadingStateLiveData)
        .build()
    usersListRxApi =
        Builder<Envelop<List<User>>>(application)
            .attachLoadingLiveData(loadingStateLiveData)
            .configureWithRx()
            .build()
  }

  /**
   * Api call with EasyApi
   * Expose the LiveData Products query so the UI can observe it.
   */
  fun fetchUsers() {
    usersListApi?.initCall(ApiFactory.instance!!.fetchUsers("1"))!!
        .execute(true) { response, isError: Boolean ->
          if (!isError)
            usersLiveData.value = response?.data
        }
  }

  /**
   * Api call with EasyApiRx
   * Expose the LiveData Products query so the UI can observe it.
   */
  fun fetchUsersRx() {
    usersListRxApi?.initCall(ApiFactory.instance!!.fetchUsersWithRx("1"))!!
        .execute(true) { response, isError ->
          if (!isError)
            usersLiveData.value = response?.data
        }
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
    usersListApi?.dispose()
    usersListRxApi?.dispose()
  }

  fun getLoadingStateLiveData(): MutableLiveData<STATE>? {
    return loadingStateLiveData
  }

//    /*Extension function*/
//    private fun <T : Base<*>> EasyApi<T>.execute() {
//    }
}
