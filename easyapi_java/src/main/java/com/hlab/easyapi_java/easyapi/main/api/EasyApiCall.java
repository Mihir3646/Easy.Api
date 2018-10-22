package com.hlab.easyapi_java.easyapi.main.api;

import android.support.annotation.NonNull;
import com.hlab.easyapi_java.easyapi.util.ResponseHandler;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;

public interface EasyApiCall<T> {
  default EasyApi<T> initCall(@NonNull Call<T> call) {
    return null;
  }

  default EasyApiRx<T> initCall(@NonNull Observable<T> call) {
    return null;
  }

  default void executeWith(@NonNull DisposableObserver<T> observer) {
  }

  default void executeWith(@NonNull Consumer<T> successConsumer,
      @NonNull Consumer<Throwable> failureConsumer) {
  }

  void execute(boolean showErrorMessages, @NonNull ResponseHandler<T> responseHandler);

  void dispose();
}
