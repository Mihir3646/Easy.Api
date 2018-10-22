package com.hlabexmaples.easyapi.ui.base;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hlab.easyapi_java.easyapi.util.LoaderInterface;
import com.hlab.easyapi_java.easyapi.util.STATE;
import com.hlabexmaples.easyapi.R;
import com.hlabexmaples.easyapi.common.DialogUtils;
import java.util.Objects;

/**
 * Base class for all the fragments used, manages common feature needed in the most of the fragments
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment
    implements View.OnClickListener, LoaderInterface {

  protected T binding;

  protected abstract int defineLayoutResource();

  protected abstract void initializeComponent(View view);//to initialize the fragments components

  protected abstract void initToolbar();

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, defineLayoutResource(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initializeComponent(view);
  }

  public void goBack() {
    if (getActivity() != null) {
      getActivity().onBackPressed();
    }
  }

  @Override
  public void showNoInternet() {
    DialogUtils.displayDialog(getActivity(), getString(R.string.alert_no_connection));
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void showError(String message) {
    DialogUtils.displayDialog(getActivity(), message);
  }

  public void observeLoadingState(MutableLiveData<STATE> state) {
    if (state != null) {
      state.observe(this, state1 -> {
        switch (Objects.requireNonNull(state1)) {
          case SHOW_LOADING:
            showLoading();
            break;
          case HIDE_LOADING:
            hideLoading();
            break;
          case ERROR:
            showError("");
            break;
          case NO_INTERNET:
            showNoInternet();
            break;
        }
      });
    }
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {
      initToolbar();
    }
  }

  /**
   * Adds the Fragment into layout container.
   *
   * @param container Resource id of the layout in which Fragment will be added
   * @param currentFragment Current loaded Fragment to be hide
   * @param nextFragment New Fragment to be loaded into container
   */
  protected void add(final int container, final Fragment currentFragment,
      final Fragment nextFragment) {
    if (getFragmentManager() != null) {
      getFragmentManager().beginTransaction()
          .add(container, nextFragment, nextFragment.getClass().getSimpleName())
          .hide(currentFragment)
          .addToBackStack(null)
          .commit();
    }
  }
}