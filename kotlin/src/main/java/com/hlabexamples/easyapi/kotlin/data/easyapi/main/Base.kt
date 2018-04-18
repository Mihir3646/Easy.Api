package com.hlabexamples.easyapi.kotlin.data.easyapi.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by H.T. on 05/01/18.
 */

open class Base<T>(
        @SerializedName("success")
        @Expose
        var isSuccess: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: String? = null,
        @SerializedName("data")
        @Expose
        var data: T? = null)

class ModelError {
    @SerializedName("Status")
    @Expose
    var status: ModelStatus? = null
}

class ModelStatus {

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null
}
