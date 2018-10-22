package com.hlab.easyapi_java.easyapi.main.api;

import android.support.annotation.NonNull;
import com.hlab.easyapi_java.R;
import com.hlab.easyapi_java.easyapi.util.NetworkUtils;
import com.hlab.easyapi_java.easyapi.util.ResponseHandler;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by H.T. on 05/01/18.
 */

public class EasyApiRx<T> extends EasyApiBase implements EasyApiCall<T> {

  private Observable<T> call;
  private DisposableObserver<T> disposable;

  EasyApiRx(Config config) {
    this.config = config;
  }

  @Override
  public EasyApiRx<T> initCall(@NonNull Observable<T> call) {
    this.call = call;
    return this;
  }

  @Override
  public void execute(boolean showErrorMessages, @NonNull ResponseHandler<T> responseHandler) {
    DisposableObserver<T> disposable = getDisposableObserver(responseHandler, showErrorMessages);

    Observable<T> source = getSource();

    if (source != null) {
      showProgress();

      source.subscribe(disposable);
      this.disposable = disposable;
    }
  }

  @Override
  public void executeWith(@NonNull DisposableObserver<T> observer) {
    Observable<T> source = getSource();

    if (source != null) {
      source.subscribe(observer);
    }
  }

  @Override
  public void executeWith(@NonNull Consumer<T> successConsumer,
      @NonNull Consumer<Throwable> failureConsumer) {
    Observable<T> source = getSource();

    if (source != null) {
      source.subscribe(successConsumer, failureConsumer);
    }
  }

  private Observable<T> getSource() {
    return NetworkUtils.isNetworkAvailable(config.getmContext()).filter(available -> {
      if (!available) {
        showNoInternet();
      }
      return available;
    }).switchMap(available -> {
      if (available) {
        return call.compose(applyObservableAsync());
      }
      return Observable.empty();
    });
  }

  @NonNull
  private DisposableObserver<T> getDisposableObserver(ResponseHandler<T> responseHandler,
      boolean showErrorMessages) {
    return new DisposableObserver<T>() {
      @Override
      public void onNext(T responseBody) {
        try {
          if (responseBody != null) {
            responseHandler.onResponse(responseBody, true);
          } else {
            responseHandler.onResponse(null, false);
            if (showErrorMessages) {
              showError(config.getmContext().getString(R.string.alert_message_error));
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          responseHandler.onResponse(null, false);
          if (showErrorMessages) {
            showError(config.getmContext().getString(R.string.alert_message_error));
          }
        }
      }

      @Override
      public void onError(Throwable e) {
        responseHandler.onResponse(null, false);
        if (showErrorMessages) {
          showError(NetworkUtils.handleErrorResponse(e));
        }
      }

      @Override
      public void onComplete() {
        hideProgress();
      }
    };
  }

  @Override
  public void dispose() {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  /**
   * Generate reusable observable source object.
   *
   * @return ObservableTransformer
   */
  private <F> ObservableTransformer<F, F> applyObservableAsync() {
    return observable -> observable.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
