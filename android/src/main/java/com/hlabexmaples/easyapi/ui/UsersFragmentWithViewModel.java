package com.hlabexmaples.easyapi.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.TabLayout;
import android.view.View;
import com.hlabexmaples.easyapi.R;
import com.hlabexmaples.easyapi.databinding.FragmentUsersBinding;
import com.hlabexmaples.easyapi.ui.base.BaseFragment;
import com.hlabexmaples.easyapi.ui.viewmodel.UserViewModel;

/**
 * Created by H.T. on 22/02/18.
 */

public class UsersFragmentWithViewModel extends BaseFragment<FragmentUsersBinding>
    implements TabLayout.OnTabSelectedListener {

  private UserListAdapter adapter;
  private UserViewModel viewModel;

  @Override
  protected int defineLayoutResource() {
    return R.layout.fragment_users;
  }

  @Override
  protected void initializeComponent(View view) {

    viewModel = ViewModelProviders.of(this).get(UserViewModel.class);

    adapter = new UserListAdapter();
    binding.rvUsers.setAdapter(adapter);

    binding.tab.addOnTabSelectedListener(this);

    viewModel.getUsersLiveData().observe(this, users -> {
      if (users != null && users.size() > 0) {
        hideLoading();
        adapter.setUserList(users);
      } else {
        showLoading();
      }
      binding.executePendingBindings();
    });
    observeLoadingState(viewModel.getLoadingStateLiveData());

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

  private void fetchUsers() {
    viewModel.resetData();
    viewModel.fetchUsers();
  }

  private void fetchUsersRx() {
    viewModel.resetData();
    viewModel.fetchUsersRx();
  }

  /*Example*/
  private void fetchNextUsers() {
    viewModel.setNextPage();
    viewModel.fetchUsersRx();
  }

  @Override
  public void onClick(View view) {

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    viewModel.onDestroy();
  }
}

