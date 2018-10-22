package com.hlabexmaples.easyapi

import android.app.Application

/**
 * Created by H.T. on 22/02/18.
 */

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    var instance: App? = null
      internal set
  }
}
