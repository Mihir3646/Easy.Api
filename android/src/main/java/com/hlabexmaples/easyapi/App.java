package com.hlabexmaples.easyapi;

import android.app.Application;

/**
 * Created by H.T. on 22/02/18.
 */

public class App extends Application {
  static App instance;

  public static App getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
  }
}
