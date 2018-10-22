package com.hlab.easyapi_java.easyapi.main.api;

import android.support.annotation.NonNull;
import com.hlab.easyapi_java.R;
import com.hlab.easyapi_java.easyapi.util.NetworkUtils;
import com.hlab.easyapi_java.easyapi.util.ResponseHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by H.T. on 05/01/18.
 */

public class EasyApi<T> extends EasyApiBase implements EasyApiCall<T> {

  private Call<T> call;

  EasyApi(Config config) {
    this.config = config;
  }

  @Override
  public EasyApi<T> initCall(@NonNull Call<T> call) {
    this.call = call;
    return this;
  }

  @Override
  public void execute(boolean showErrorMessages, @NonNull ResponseHandler<T> responseHandler) {
    if (NetworkUtils.isNetworkOn(config.getmContext())) {
      Callback<T> callback = new Callback<T>() {

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
          hideProgress();

          try {
            if (response.isSuccessful()) {
              T responseBody = response.body();
              if (responseBody != null) {
                //TODO Handle extra data for checking success, message etc from base class
                responseHandler.onResponse(responseBody, false);
              } else {
                responseHandler.onResponse(null, true);
                if (showErrorMessages) {
                  showError(config.getmContext().getString(R.string.alert_message_error));
                }
              }
            } else {
              responseHandler.onResponse(null, true);
              if (showErrorMessages) {
                showError(config.getmContext().getString(R.string.alert_message_error));
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
            responseHandler.onResponse(null, true);
            if (showErrorMessages) {
              showError(config.getmContext().getString(R.string.alert_message_error));
            }
          }
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
          hideProgress();
          responseHandler.onResponse(null, true);
          if (showErrorMessages) {
            showError(config.getmContext().getString(R.string.alert_message_error));
          }
        }
      };

      showProgress();
      call.enqueue(callback);
    } else {
      showNoInternet();
    }
  }

  @Override public void dispose() {
    if (call != null && !call.isCanceled()) {
      call.cancel();
    }
  }
}
