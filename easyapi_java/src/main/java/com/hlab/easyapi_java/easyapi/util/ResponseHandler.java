package com.hlab.easyapi_java.easyapi.util;

public interface ResponseHandler<E> {
  void onResponse(E response, boolean isError);
}