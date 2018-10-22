package com.hlabexmaples.easyapi.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by H.T. on 05/01/18.
 */

public class Base<T> {

  @SerializedName("success")
  @Expose
  private boolean success;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("data")
  @Expose
  private T data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
