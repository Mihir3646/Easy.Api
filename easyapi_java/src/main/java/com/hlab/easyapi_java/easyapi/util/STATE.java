package com.hlab.easyapi_java.easyapi.util;

public enum STATE {
  SHOW_LOADING, HIDE_LOADING, NO_INTERNET, ERROR;

  private String msg;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
