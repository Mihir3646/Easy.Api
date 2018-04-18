package com.hlabexmaples.easyapi.ui;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import com.hlabexmaples.easyapi.R;
import com.hlabexmaples.easyapi.common.DialogUtils;
import com.hlabexmaples.easyapi.databinding.FragmentMethodSelectionBinding;
import com.hlabexmaples.easyapi.ui.base.BaseFragment;
import com.hlabexmaples.easyapi.ui.base.Module;

/**
 * Created by H.T. on 22/02/18.
 */

public class MethodSelectionFragment extends BaseFragment<FragmentMethodSelectionBinding> {

    private boolean isSelected = false;
    private Module selectedModule = Module.MVC;

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_method_selection;
    }

    @Override
    protected void initializeComponent(View view) {
        binding.btnGo.setOnClickListener(this);
        binding.setListener(this);
    }

    @Override
    protected void initToolbar() {
        assert getActivity() != null;
        binding.toolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnGo) {
            if (isSelected) {
                switch (selectedModule) {
                    case MVC:
                        add(R.id.container, this, new UsersFragment());
                        break;
                    case MVVM:
                        add(R.id.container, this, new UsersFragmentWithViewModel());
                        break;
                }
            } else {
                DialogUtils.showToast(getActivity(), "Please select one pattern");
            }
        } else if (v == binding.cvMvc) {
            isSelected = true;
            selectedModule = Module.MVC;
            showReveal(binding.cvMvc, binding.viewBehind);
            binding.cvMvc.setSelected(true);
            binding.cvMvvm.setSelected(false);
        } else if (v == binding.cvMvvm) {
            isSelected = true;
            selectedModule = Module.MVVM;
            showReveal(binding.cvMvvm, binding.viewBehind);
            binding.cvMvc.setSelected(false);
            binding.cvMvvm.setSelected(true);
        }
    }

    private void showReveal(View centerView, View viewToReveal) {
        // get the center for the clipping circle
        int cx = (int) (centerView.getX() + centerView.getWidth() / 2);
        int cy = (int) (centerView.getY() + centerView.getHeight() / 2);
        // get the final radius for the clipping circle
        float finalRadius = Math.max((float) Math.hypot(cx, cy), 1500);

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(viewToReveal, cx, cy, centerView.getWidth() / 1.8f, finalRadius);
        anim.setDuration(700).setInterpolator(new AccelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                viewToReveal.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                viewToReveal.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        // make the view visible and start the animation
        viewToReveal.setVisibility(View.VISIBLE);
        // start animation
        anim.start();
    }
}
