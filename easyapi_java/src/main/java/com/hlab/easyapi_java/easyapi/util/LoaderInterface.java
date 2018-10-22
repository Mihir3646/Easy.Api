package com.hlab.easyapi_java.easyapi.util;

/**
 * Created by H.T. on 05/01/18.
 */

public interface LoaderInterface {
  void showLoading();

  void hideLoading();

  void showNoInternet();

  void showError(String message);
}
