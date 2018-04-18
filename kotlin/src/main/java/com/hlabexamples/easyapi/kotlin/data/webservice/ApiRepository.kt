package com.hlabexamples.easyapi.kotlin.data.webservice

import com.hlabexamples.easyapi.kotlin.data.easyapi.main.Base
import com.hlabexamples.easyapi.kotlin.data.models.Envelop
import com.hlabexamples.easyapi.kotlin.data.models.User
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit callback methods.
 */

interface ApiRepository {

    @GET(Constants.WS_METHOD_USERS)
    fun fetchUsers(@Query(Constants.PARAM_PAGE) page: String): Call<Envelop<List<User>>>

    @GET(Constants.WS_METHOD_USERS)
    fun fetchUsersWithRx(@Query(Constants.PARAM_PAGE) page: String): Observable<Envelop<List<User>>>

    /*Example with Base class*/
    @GET(Constants.WS_METHOD_USERS)
    fun fetchUsersBase(@Query(Constants.PARAM_PAGE) page: String): Observable<Base<List<User>>>

}
