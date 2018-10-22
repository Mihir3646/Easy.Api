package com.hlabexmaples.easyapi.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;
import com.hlab.easyapi_java.easyapi.main.api.EasyApi;
import com.hlab.easyapi_java.easyapi.main.api.EasyApiCall;
import com.hlab.easyapi_java.easyapi.util.NetworkUtils;
import com.hlabexmaples.easyapi.R;
import com.hlabexmaples.easyapi.data.models.Envelop;
import com.hlabexmaples.easyapi.data.models.User;
import com.hlabexmaples.easyapi.data.webservice.ApiFactory;
import com.hlabexmaples.easyapi.databinding.FragmentUsersBinding;
import com.hlabexmaples.easyapi.ui.base.BaseFragment;
import io.reactivex.observers.DisposableObserver;
import java.util.List;
import java.util.Objects;

/**
 * Created by H.T. on 22/02/18.
 */

public class UsersFragment extends BaseFragment<FragmentUsersBinding>
    implements TabLayout.OnTabSelectedListener {

  private UserListAdapter adapter;
  private EasyApiCall<Envelop<List<User>>> usersListApi;
  private EasyApiCall<Envelop<List<User>>> usersListRxApi;

  @Override
  protected int defineLayoutResource() {
    return R.layout.fragment_users;
  }

  @Override
  protected void initializeComponent(View view) {
    //EasyApi Configuration
    usersListApi = new EasyApi.Builder<Envelop<List<User>>>(Objects.requireNonNull(getActivity()))
        .setLoaderInterface(this).build();
    usersListRxApi = new EasyApi.Builder<Envelop<List<User>>>(Objects.requireNonNull(getActivity()))
        .setLoaderInterface(this).configureWithRx().build();

    adapter = new UserListAdapter();
    binding.rvUsers.setAdapter(adapter);

    binding.tab.addOnTabSelectedListener(this);

    fetchUsers();
  }

  @Override
  protected void initToolbar() {
    assert getActivity() != null;
    binding.toolbar.setTitle(getString(R.string.title_users));
    binding.toolbar.setNavigationIcon(R.drawable.ic_back);
    binding.toolbar.setNavigationOnClickListener(view -> goBack());
  }

  @Override
  public void onClick(View v) {

  }

  /**
   * Example 1:
   * Load via normal retrofit call
   */
  private void fetchUsers() {
    usersListApi
        .initCall(ApiFactory.getInstance().fetchUsers("1"))
        .execute(true, (response, isError) -> {
          if (!isError) {
            adapter.setUserList(response.getData());
          }
        });
  }

  /**
   * Example 2: (default)
   * Load via normal RxJava call
   */
  private void fetchUsersRx() {
    usersListRxApi.initCall(ApiFactory.getInstance().fetchUsersWithRx("1"))
        .execute(true, (response, isError) -> {
          if (!isError) {
            adapter.setUserList(response.getData());
          }
        });
  }

  /**
   * Example 3:
   * Load via custom RxJava observer
   */
  private void fetchUsersRxExample1() {
    DisposableObserver<Envelop<List<User>>> observer = getExample1Observer();

    showLoading();
    usersListRxApi.initCall(ApiFactory.getInstance().fetchUsersWithRx("1"))
        .executeWith(observer);
  }

  /**
   * Example 4:
   * Load via RxJava Consumer Callbacks
   */
  private void fetchUsersRxExample2() {
    showLoading();
    usersListRxApi.initCall(ApiFactory.getInstance().fetchUsersWithRx("1"))
        .executeWith(value -> {
          hideLoading();
          if (value.getData() != null) {
            adapter.setUserList(value.getData());
          } else {
            showError(getString(R.string.alert_message_error));
          }
        }, e -> {
          hideLoading();
          NetworkUtils.handleErrorResponse(e);
        });
  }

  @NonNull
  private DisposableObserver<Envelop<List<User>>> getExample1Observer() {
    return new DisposableObserver<Envelop<List<User>>>() {

      @Override
      public void onNext(Envelop<List<User>> value) {
        if (value.getData() != null) {
          adapter.setUserList(value.getData());
        } else {
          showError(getString(R.string.alert_message_error));
        }
      }

      @Override
      public void onError(Throwable e) {
        NetworkUtils.handleErrorResponse(e);
      }

      @Override
      public void onComplete() {
        hideLoading();
      }
    };
  }

  @Override
  public void showLoading() {
    binding.setIsLoading(true);
  }

  @Override
  public void hideLoading() {
    binding.setIsLoading(false);
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    int tag = tab.getPosition();
    switch (tag) {
      case 0:
        fetchUsers();
        break;
      case 1:
        fetchUsersRx();
        break;
    }
  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    usersListApi.dispose();
    usersListRxApi.dispose();
  }
}
