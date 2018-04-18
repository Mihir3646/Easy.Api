package com.hlabexmaples.easyapi.data.easyapi.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hlabexmaples.easyapi.data.easyapi.main.model.Base;
import com.hlabexmaples.easyapi.data.easyapi.util.LoaderInterface;
import com.hlabexmaples.easyapi.data.easyapi.util.NetworkUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by H.T. on 05/01/18.
 */

public class EasyApiRx<T extends Base> {

    private Context context;
    private LoaderInterface loaderInterface;
    private Observable<T> call;

    public EasyApiRx(@NonNull Context context) {
        this.context = context;
    }

    public EasyApiRx<T> setLoaderInterface(@NonNull LoaderInterface loaderInterface) {
        this.loaderInterface = loaderInterface;
        return this;
    }

    public EasyApiRx<T> initCall(@NonNull Observable<T> call) {
        this.call = call;
        return this;
    }

    public DisposableObserver<T> execute(@NonNull ResponseHandler<T> responseHandler) {
        DisposableObserver<T> disposable = getDisposableObserver(responseHandler);

        Observable<T> source = getSource();

        if (source != null) {
            if (loaderInterface != null)
                loaderInterface.showLoading();

            source.subscribe(disposable);
            return disposable;
        }
        return null;

    }

    public void executeWith(@NonNull DisposableObserver<T> observer) {
        Observable<T> source = getSource();

        if (source != null) {
            source.subscribe(observer);
        }
    }

    public void executeWith(@NonNull Consumer<T> successConsumer, @NonNull Consumer<Throwable> failureConsumer) {
        Observable<T> source = getSource();

        if (source != null) {
            source.subscribe(successConsumer, failureConsumer);
        }
    }

    private Observable<T> getSource() {
        return NetworkUtils.isNetworkAvailable(context).filter(available -> {
            if (!available)
                loaderInterface.showNoInternet();
            return available;
        }).switchMap(available -> {
            if (available)
                return call.compose(applyObservableAsync());
            return Observable.empty();
        });
    }

    @NonNull
    private DisposableObserver<T> getDisposableObserver(ResponseHandler<T> responseHandler) {
        return new DisposableObserver<T>() {
            @Override
            public void onNext(T responseBody) {
                try {
                    if (responseBody != null) {
                        //TODO Handle extra data for checking success, message etc from base class
                        responseHandler.onResponse(responseBody,
                                /*((T) responseBody.getData()).isSuccess()*/ true,
                                responseBody.getMessage());
                    } else
                        NetworkUtils.showApiError(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    NetworkUtils.showApiError(context);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (loaderInterface != null) {
                    loaderInterface.showError(NetworkUtils.handleErrorResponse(e));
                }
            }

            @Override
            public void onComplete() {
                if (loaderInterface != null)
                    loaderInterface.hideLoading();
            }
        };
    }


    /**
     * Generate reusable observable source object.
     *
     * @return ObservableTransformer
     */
    private <F> ObservableTransformer<F, F> applyObservableAsync() {
        return observable -> observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
