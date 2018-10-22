package com.hlab.easyapi_kotlin.easyapi.main.api

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.hlab.easyapi_kotlin.easyapi.util.LoaderInterface
import com.hlab.easyapi_kotlin.easyapi.util.STATE

class Config(private var mContext: Context) {
  var mLoaderInterface: LoaderInterface? = null

  var isRxCall = false

  //While using live data
  var mProgressLiveData: MutableLiveData<STATE>? = null
  var isUsingLiveData = false

  fun getmContext(): Context {
    return mContext
  }

  fun setmContext(mContext: Context) {
    this.mContext = mContext
  }

}
