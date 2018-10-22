package com.hlabexmaples.easyapi.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import com.hlab.easyapi_java.easyapi.main.api.EasyApi;
import com.hlab.easyapi_java.easyapi.main.api.EasyApiCall;
import com.hlab.easyapi_java.easyapi.util.STATE;
import com.hlabexmaples.easyapi.data.models.Envelop;
import com.hlabexmaples.easyapi.data.models.User;
import com.hlabexmaples.easyapi.data.webservice.ApiFactory;
import java.util.List;
import java.util.Objects;

/**
 * Created by H.T. on 22/02/18.
 */

public class UserViewModel extends AndroidViewModel {

  private MutableLiveData<List<User>> usersLiveData;
  private MutableLiveData<STATE> loadingStateLiveData;

  private EasyApiCall<Envelop<List<User>>> usersListApi;
  private EasyApiCall<Envelop<List<User>>> usersListRxApi;

  private int pageNo = 1;

  public UserViewModel(Application application) {
    super(application);

    // initialize live data
    usersLiveData = new MutableLiveData<>();
    loadingStateLiveData = new MutableLiveData<>();
    // set by default null
    usersLiveData.setValue(null);

    //EasyApi Configuration
    usersListApi = new EasyApi.Builder<Envelop<List<User>>>(Objects.requireNonNull(application))
        .attachLoadingLiveData(loadingStateLiveData).build();
    usersListRxApi = new EasyApi.Builder<Envelop<List<User>>>(Objects.requireNonNull(application))
        .attachLoadingLiveData(loadingStateLiveData).configureWithRx().build();
  }

  /**
   * Api call with EasyApi
   * Expose the LiveData Products query so the UI can observe it.
   */
  public void fetchUsers() {
    usersListApi.initCall(ApiFactory.getInstance().fetchUsers(String.valueOf(pageNo)))
        .execute(true, (response, isError) -> {
          if (!isError) {
            usersLiveData.setValue(response.getData());
          }
        });
  }

  /**
   * Api call with EasyApiRx
   * Expose the LiveData Products query so the UI can observe it.
   */
  public void fetchUsersRx() {
    usersListRxApi.initCall(ApiFactory.getInstance().fetchUsersWithRx(String.valueOf(pageNo)))
        .execute(true, (response, isError) -> {
          if (!isError) {
            usersLiveData.setValue(response.getData());
          }
        });
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

  public MutableLiveData<List<User>> getUsersLiveData() {
    return usersLiveData;
  }

  public void onDestroy() {
    usersListApi.dispose();
    usersListRxApi.dispose();
  }

  public MutableLiveData<STATE> getLoadingStateLiveData() {
    return loadingStateLiveData;
  }
}
