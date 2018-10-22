package com.hlab.easyapi_java.easyapi.main.api;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import com.hlab.easyapi_java.easyapi.util.LoaderInterface;
import com.hlab.easyapi_java.easyapi.util.STATE;

abstract class EasyApiBase {

  Config config;

  void showProgress() {
    if (config.isUsingLiveData()) {
      if (config.getmProgressLiveData() != null) {
        config.getmProgressLiveData().postValue(STATE.SHOW_LOADING);
      }
    } else if (config.getmLoaderInterface() != null) {
      config.getmLoaderInterface().showLoading();
    }
  }

  void hideProgress() {
    if (config.isUsingLiveData()) {
      if (config.getmProgressLiveData() != null) {
        config.getmProgressLiveData().postValue(STATE.HIDE_LOADING);
      }
    } else if (config.getmLoaderInterface() != null) {
      config.getmLoaderInterface().hideLoading();
    }
  }

  void showNoInternet() {
    if (config.isUsingLiveData()) {
      if (config.getmProgressLiveData() != null) {
        config.getmProgressLiveData().postValue(STATE.NO_INTERNET);
      }
    } else if (config.getmLoaderInterface() != null) {
      config.getmLoaderInterface().showNoInternet();
    }
  }

  void showError(String error) {
    if (config.isUsingLiveData()) {
      if (config.getmProgressLiveData() != null) {
        STATE state = STATE.ERROR;
        state.setMsg(error);
        config.getmProgressLiveData().postValue(state);
      }
    } else if (config.getmLoaderInterface() != null) {
      config.getmLoaderInterface().showError(error);
    }
  }

  public static class Builder<T> {
    private Config config;

    public Builder(@NonNull Context context) {
      config = new Config(context);
    }

    public Builder<T> setLoaderInterface(@NonNull LoaderInterface loaderInterface) {
      config.setmLoaderInterface(loaderInterface);
      return this;
    }

    public Builder<T> attachLoadingLiveData(MutableLiveData<STATE> progressData) {
      config.setUsingLiveData(true);
      config.setmProgressLiveData(progressData);
      return this;
    }

    public Builder<T> configureWithRx() {
      config.setRxCall(true);
      return this;
    }

    public EasyApiCall<T> build() {
      if (!config.isRxCall()) {
        return new EasyApi<>(config);
      } else {
        return new EasyApiRx<>(config);
      }
    }
  }
}
