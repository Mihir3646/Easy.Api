package com.hlabexmaples.easyapi.data.easyapi.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hlabexmaples.easyapi.data.easyapi.main.model.Base;
import com.hlabexmaples.easyapi.data.easyapi.util.LoaderInterface;
import com.hlabexmaples.easyapi.data.easyapi.util.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by H.T. on 05/01/18.
 */

public class EasyApi<T extends Base> {

    private Context context;
    private LoaderInterface loaderInterface;
    private Call<T> call;

    public EasyApi(@NonNull Context context) {
        this.context = context;
    }

    public EasyApi<T> setLoaderInterface(@NonNull LoaderInterface loaderInterface) {
        this.loaderInterface = loaderInterface;
        return this;
    }

    public EasyApi<T> initCall(@NonNull Call<T> call) {
        this.call = call;
        return this;
    }

    public void execute(@NonNull ResponseHandler<T> responseHandler) {
        if (NetworkUtils.isNetworkOn(context)) {
            Callback<T> callback = new Callback<T>() {

                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    if (loaderInterface != null)
                        loaderInterface.hideLoading();

                    try {
                        if (response.isSuccessful()) {
                            T responseBody = response.body();
                            if (responseBody != null) {
                                //TODO Handle extra data for checking success, message etc from base class
                                responseHandler.onResponse(responseBody,
                                        /*((T) responseBody.getData()).isSuccess()*/ true,
                                        responseBody.getMessage());
                            } else
                                NetworkUtils.showApiError(context);
                        } else
                            NetworkUtils.showApiError(context);

                    } catch (Exception e) {
                        e.printStackTrace();
                        NetworkUtils.showApiError(context);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    if (loaderInterface != null) {
                        loaderInterface.hideLoading();
                        loaderInterface.showError(NetworkUtils.handleErrorResponse(t));
                    }
                }
            };

            if (loaderInterface != null)
                loaderInterface.showLoading();

            call.enqueue(callback);
        } else
            loaderInterface.showNoInternet();
    }
}
