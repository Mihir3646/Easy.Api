package com.hlab.easyapi_java.easyapi.main.api;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import com.hlab.easyapi_java.easyapi.util.LoaderInterface;
import com.hlab.easyapi_java.easyapi.util.STATE;

public class Config {

  private Context mContext;
  private LoaderInterface mLoaderInterface;
  private boolean isRxCall = false;

  //While using live data
  private MutableLiveData<STATE> mProgressLiveData;
  private boolean isUsingLiveData = false;

  public Config(Context mContext) {
    this.mContext = mContext;
  }

  public Context getmContext() {
    return mContext;
  }

  public void setmContext(Context mContext) {
    this.mContext = mContext;
  }

  public LoaderInterface getmLoaderInterface() {
    return mLoaderInterface;
  }

  public void setmLoaderInterface(LoaderInterface mLoaderInterface) {
    this.mLoaderInterface = mLoaderInterface;
  }

  public MutableLiveData<STATE> getmProgressLiveData() {
    return mProgressLiveData;
  }

  public void setmProgressLiveData(
      MutableLiveData<STATE> mProgressLiveData) {
    this.mProgressLiveData = mProgressLiveData;
  }

  public boolean isUsingLiveData() {
    return isUsingLiveData;
  }

  public void setUsingLiveData(boolean usingLiveData) {
    isUsingLiveData = usingLiveData;
  }

  public boolean isRxCall() {
    return isRxCall;
  }

  public void setRxCall(boolean rxCall) {
    isRxCall = rxCall;
  }
}
