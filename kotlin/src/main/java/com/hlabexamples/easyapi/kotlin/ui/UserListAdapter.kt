package com.hlabexamples.easyapi.kotlin.ui

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hlabexamples.easyapi.kotlin.common.DialogUtils
import com.hlabexamples.easyapi.kotlin.data.models.User
import com.hlabexmaples.easyapi.R
import com.hlabexmaples.easyapi.databinding.RowUserBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private var mUserList: List<User>? = null

    fun setUserList(userList: List<User>) {
        if (mUserList == null) {
            mUserList = userList
            notifyItemRangeInserted(0, userList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mUserList!!.size
                }

                override fun getNewListSize(): Int {
                    return userList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mUserList!![oldItemPosition].id == userList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newUser = userList[newItemPosition]
                    val oldUser = mUserList!![oldItemPosition]
                    return (newUser.id == oldUser.id
                            && newUser.lastName == oldUser.lastName
                            && newUser.firstName == oldUser.firstName)
                }
            })
            mUserList = userList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = DataBindingUtil.inflate<RowUserBinding>(LayoutInflater.from(parent.context), R.layout.row_user, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mUserList!![position])
    }

    override fun getItemCount(): Int {
        return if (mUserList == null) 0 else mUserList!!.size
    }

    inner class UserViewHolder(private val binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(user: User) {
            binding.data = user
            binding.click = this
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {
            DialogUtils.showToast(v.context, binding.data?.firstName!!)
        }
    }
}
