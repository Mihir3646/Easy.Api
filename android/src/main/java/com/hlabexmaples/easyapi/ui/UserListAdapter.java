package com.hlabexmaples.easyapi.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hlabexmaples.easyapi.R;
import com.hlabexmaples.easyapi.common.DialogUtils;
import com.hlabexmaples.easyapi.data.models.User;
import com.hlabexmaples.easyapi.databinding.RowUserBinding;
import java.util.List;
import java.util.Objects;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

  private List<User> mUserList;

  public void setUserList(final List<User> userList) {
    if (mUserList == null) {
      mUserList = userList;
      notifyItemRangeInserted(0, userList.size());
    } else {
      DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
        @Override
        public int getOldListSize() {
          return mUserList.size();
        }

        @Override
        public int getNewListSize() {
          return userList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
          return mUserList.get(oldItemPosition).getId() ==
              userList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
          User newUser = userList.get(newItemPosition);
          User oldUser = mUserList.get(oldItemPosition);
          return newUser.getId() == oldUser.getId()
              && Objects.equals(newUser.getLastName(), oldUser.getLastName())
              && Objects.equals(newUser.getFirstName(), oldUser.getFirstName());
        }
      });
      mUserList = userList;
      result.dispatchUpdatesTo(this);
    }
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    RowUserBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_user, parent,
            false);
    return new UserViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    holder.bind(mUserList.get(position));
  }

  @Override
  public int getItemCount() {
    return mUserList == null ? 0 : mUserList.size();
  }

  class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final RowUserBinding binding;

    UserViewHolder(RowUserBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(User user) {
      binding.setData(user);
      binding.setClick(this);
      binding.executePendingBindings();
    }

    @Override
    public void onClick(View v) {
      DialogUtils.showToast(v.getContext(), binding.getData().getFirstName());
    }
  }
}
