package com.hlab.easyapi_kotlin.easyapi.util

enum class STATE {
  SHOW_LOADING,
  HIDE_LOADING,
  NO_INTERNET,
  ERROR;

  var msg: String? = null
}
