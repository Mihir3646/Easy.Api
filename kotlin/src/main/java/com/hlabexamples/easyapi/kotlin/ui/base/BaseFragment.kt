package com.hlabexamples.easyapi.kotlin.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hlab.easyapi_kotlin.easyapi.util.LoaderInterface
import com.hlab.easyapi_kotlin.easyapi.util.STATE
import com.hlabexamples.easyapi.kotlin.common.DialogUtils
import com.hlabexmaples.easyapi.R

/**
 * Base class for all the fragments used, manages common feature needed in the most of the fragments
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment(),
    View.OnClickListener,
    LoaderInterface {

  protected lateinit var binding: T

  protected abstract fun defineLayoutResource(): Int

  protected abstract fun initializeComponent(view: View) //to initialize the fragments components

  protected abstract fun initToolbar()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, defineLayoutResource(), container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initToolbar()
    initializeComponent(view)
  }

  fun goBack() {
    if (activity != null) {
      activity!!.onBackPressed()
    }
  }

  override fun showNoInternet() {
    DialogUtils.displayDialog(context!!, getString(R.string.alert_no_connection))
  }

  override fun showLoading() {

  }

  override fun hideLoading() {

  }

  override fun showError(message: String) {
    DialogUtils.displayDialog(context!!, message)
  }

  fun observeLoadingState(state: MutableLiveData<STATE>?) {
    state?.observe(this, Observer<STATE> { state1 ->
      state1?.let {
        when (it) {
          STATE.SHOW_LOADING -> showLoading()
          STATE.HIDE_LOADING -> hideLoading()
          STATE.ERROR -> showError("")
          STATE.NO_INTERNET -> showNoInternet()
        }
      }
    })
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)
    if (!hidden)
      initToolbar()
  }

  /**
   * Adds the Fragment into layout container.
   *
   * @param container       Resource id of the layout in which Fragment will be added
   * @param currentFragment Current loaded Fragment to be hide
   * @param nextFragment    New Fragment to be loaded into container
   */
  protected fun add(
    container: Int,
    currentFragment: Fragment,
    nextFragment: Fragment
  ) {
    if (fragmentManager != null) {
      fragmentManager!!.beginTransaction()
          .add(container, nextFragment, nextFragment.javaClass.simpleName)
          .hide(currentFragment)
          .addToBackStack(null)
          .commit()
    }
  }
}