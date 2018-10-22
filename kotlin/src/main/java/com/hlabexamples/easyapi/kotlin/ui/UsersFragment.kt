package com.hlabexamples.easyapi.kotlin.ui

import android.support.design.widget.TabLayout
import android.view.View
import com.hlab.easyapi_kotlin.easyapi.main.api.EasyApiBase.Builder
import com.hlab.easyapi_kotlin.easyapi.main.api.EasyApiCall
import com.hlab.easyapi_kotlin.easyapi.util.NetworkUtils
import com.hlabexamples.easyapi.kotlin.data.models.Envelop
import com.hlabexamples.easyapi.kotlin.data.models.User
import com.hlabexamples.easyapi.kotlin.data.webservice.ApiFactory
import com.hlabexamples.easyapi.kotlin.ui.base.BaseFragment
import com.hlabexmaples.easyapi.R
import com.hlabexmaples.easyapi.databinding.FragmentUsersBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver

/**
 * Created by H.T. on 22/02/18.
 */

class UsersFragment : BaseFragment<FragmentUsersBinding>(), TabLayout.OnTabSelectedListener {

  private var disposable: CompositeDisposable? = null
  private var adapter: UserListAdapter? = null
  private var usersListApi: EasyApiCall<Envelop<List<User>>>? = null
  private var usersListRxApi: EasyApiCall<Envelop<List<User>>>? = null

  override fun defineLayoutResource(): Int {
    return R.layout.fragment_users
  }

  override fun initializeComponent(view: View) {
    disposable = CompositeDisposable()
    usersListApi = Builder<Envelop<List<User>>>(activity!!)
        .setLoaderInterface(this)
        .build()
    usersListRxApi =
        Builder<Envelop<List<User>>>(activity!!)
            .setLoaderInterface(this)
            .configureWithRx()
            .build()

    adapter = UserListAdapter()
    binding.rvUsers.adapter = adapter

    binding.tab.addOnTabSelectedListener(this)

    fetchUsers()

  }

  override fun initToolbar() {
    assert(activity != null)
    binding.toolbar.title = getString(R.string.title_users)
    binding.toolbar.setNavigationIcon(R.drawable.ic_back)
    binding.toolbar.setNavigationOnClickListener {
      goBack()
    }
  }

  override fun onClick(v: View) {

  }

  /**
   * Example 1:
   * Load via normal retrofit call
   */
  private fun fetchUsers() {
    usersListApi?.initCall(ApiFactory.instance!!.fetchUsers("1"))!!
        .execute(true) { response, isError: Boolean ->
          if (!isError)
            response?.data?.let { adapter?.setUserList(it) }
        }
  }

  /**
   * Example 2: (default)
   * Load via normal RxJava call
   */
  private fun fetchUsersRx() {
    usersListRxApi?.initCall(ApiFactory.instance!!.fetchUsersWithRx("1"))!!
        .execute(true) { response, isError ->
          if (!isError)
            response?.data?.let { adapter?.setUserList(it) }
        }
  }

  /**
   * Example 3:
   * Load via custom RxJava observer
   */
  private fun fetchUsersRxExample1() {
    val observer = getExample1Observer()

    showLoading()
    usersListRxApi?.initCall(ApiFactory.instance!!.fetchUsersWithRx("1"))!!
        .executeWith(observer)
  }

  /**
   * Example 4:
   * Load via RxJava Consumer Callbacks
   */
  private fun fetchUsersRxExample2() {
    showLoading()
    usersListRxApi?.initCall(ApiFactory.instance!!.fetchUsersWithRx("1"))!!
        .executeWith(Consumer { value ->
          hideLoading()
          if (value.data != null) {
            adapter?.setUserList(value.data!!)
          } else {
            showError(getString(R.string.alert_message_error))
          }
        }, Consumer { t ->
          hideLoading()
          showError(NetworkUtils.handleErrorResponse(t!!))
        })
  }

  private fun getExample1Observer(): DisposableObserver<Envelop<List<User>>> {
    return object : DisposableObserver<Envelop<List<User>>>() {

      override fun onNext(value: Envelop<List<User>>) {
        if (value.data != null) {
          adapter?.setUserList(value.data!!)
        } else
          showError(context!!.getString(R.string.alert_message_error))

      }

      override fun onError(e: Throwable) {
        NetworkUtils.handleErrorResponse(e)
      }

      override fun onComplete() {
        hideLoading()
      }
    }
  }

  override fun showLoading() {
    binding.isLoading = true
  }

  override fun hideLoading() {
    binding.isLoading = false
  }

  override fun onTabSelected(tab: TabLayout.Tab) {
    val tag = tab.position
    when (tag) {
      0 -> fetchUsers()
      1 -> fetchUsersRx()
    }
  }

  override fun onTabUnselected(tab: TabLayout.Tab) {

  }

  override fun onTabReselected(tab: TabLayout.Tab) {

  }

  override fun onDestroy() {
    super.onDestroy()
    if (disposable != null && !disposable!!.isDisposed) {
      disposable!!.dispose()
    }
  }
}
