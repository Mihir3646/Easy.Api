package com.hlabexamples.easyapi.kotlin.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.TabLayout
import android.view.View
import com.hlabexamples.easyapi.kotlin.data.models.User
import com.hlabexamples.easyapi.kotlin.ui.base.BaseFragment
import com.hlabexamples.easyapi.kotlin.ui.viewmodel.UserViewModel
import com.hlabexmaples.easyapi.R
import com.hlabexmaples.easyapi.databinding.FragmentUsersBinding

/**
 * Created by H.T. on 22/02/18.
 */

class UsersFragmentWithViewModel : BaseFragment<FragmentUsersBinding>(), TabLayout.OnTabSelectedListener {

    private lateinit var adapter: UserListAdapter
    private lateinit var viewModel: UserViewModel

    override fun defineLayoutResource(): Int {
        return R.layout.fragment_users
    }

    override fun initializeComponent(view: View) {

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        adapter = UserListAdapter()
        binding.rvUsers.adapter = adapter

        binding.tab.addOnTabSelectedListener(this)

        viewModel.usersLiveData.observe(this, Observer<List<User>> { users ->
            if (users != null && users.isNotEmpty()) {
                hideLoading()
                adapter.setUserList(users)
            } else {
                showLoading()
            }
            binding.executePendingBindings()
        })

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

    private fun fetchUsers() {
        viewModel.resetData()
        activity?.let { viewModel.fetchUsers(it, this) }
    }

    private fun fetchUsersRx() {
        viewModel.resetData()
        activity?.let { viewModel.fetchUsersRx(it, this) }
    }

    /*Example*/
    private fun fetchNextUsers() {
        activity?.let {
            viewModel.setNextPage()
            viewModel.fetchUsersRx(it, this)
        }
    }

    override fun onClick(view: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

}

