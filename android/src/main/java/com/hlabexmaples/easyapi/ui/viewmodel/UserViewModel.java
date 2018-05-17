package com.hlabexmaples.easyapi.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;

import com.hlabexmaples.easyapi.data.easyapi.main.EasyApi;
import com.hlabexmaples.easyapi.data.easyapi.main.EasyApiRx;
import com.hlabexmaples.easyapi.data.easyapi.util.LoaderInterface;
import com.hlabexmaples.easyapi.data.models.Envelop;
import com.hlabexmaples.easyapi.data.models.User;
import com.hlabexmaples.easyapi.data.webservice.ApiFactory;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by H.T. on 22/02/18.
 */

public class UserViewModel extends AndroidViewModel {
    private MediatorLiveData<List<User>> usersLiveData;
    private CompositeDisposable disposable;

    private int pageNo = 1;

    public UserViewModel(Application application) {
        super(application);

        disposable = new CompositeDisposable();
        // initialize live data
        usersLiveData = new MediatorLiveData<>();
        // set by default null
        usersLiveData.setValue(null);

    }

    /**
     * Api call with EasyApi
     * Expose the LiveData Products query so the UI can observe it.
     */
    public void fetchUsers(LoaderInterface loaderInterface) {
        new EasyApi<Envelop<List<User>>>(getApplication())
                .setLoaderInterface(loaderInterface)
                .initCall(ApiFactory.getInstance().fetchUsers(String.valueOf(pageNo)))
                .execute((response, isSuccess, successMessage) -> {
                    if (isSuccess) {
                        usersLiveData.setValue(response.getData());
                    }
                });
    }

    /**
     * Api call with EasyApiRx
     * Expose the LiveData Products query so the UI can observe it.
     */
    public void fetchUsersRx(LoaderInterface loaderInterface) {
        DisposableObserver<?> d = new EasyApiRx<Envelop<List<User>>>(getApplication())
                .initCall(ApiFactory.getInstance().fetchUsersWithRx(String.valueOf(pageNo)))
                .setLoaderInterface(loaderInterface)
                .execute((response, isSuccess, message) -> {
                    if (isSuccess) {
                        usersLiveData.setValue(response.getData());
                    }
                });
        disposable.add(d);
    }

    /**
     * Test function to reset live data, DON'T do in actual development
     */
    public void resetData() {
        usersLiveData.setValue(null);
    }

    public void setNextPage() {
        pageNo++;
    }

    public MediatorLiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


}
