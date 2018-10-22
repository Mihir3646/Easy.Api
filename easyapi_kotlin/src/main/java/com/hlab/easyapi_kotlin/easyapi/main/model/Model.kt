package com.hlab.easyapi_kotlin.easyapi.main.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
