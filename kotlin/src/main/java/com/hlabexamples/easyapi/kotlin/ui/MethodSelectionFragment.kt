package com.hlabexamples.easyapi.kotlin.ui

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.hlabexamples.easyapi.kotlin.common.DialogUtils
import com.hlabexamples.easyapi.kotlin.ui.base.BaseFragment
import com.hlabexamples.easyapi.kotlin.ui.base.Module
import com.hlabexmaples.easyapi.R
import com.hlabexmaples.easyapi.databinding.FragmentMethodSelectionBinding

/**
 * Created by H.T. on 22/02/18.
 */

class MethodSelectionFragment : BaseFragment<FragmentMethodSelectionBinding>() {

    private var isSelected = false
    private var selectedModule = Module.MVC

    override fun defineLayoutResource(): Int {
        return R.layout.fragment_method_selection
    }

    override fun initializeComponent(view: View) {
        binding.btnGo.setOnClickListener(this)
        binding.listener = this
    }

    override fun initToolbar() {
        assert(activity != null)
        binding.toolbar.title = getString(R.string.app_name)
    }

    override fun onClick(v: View) {
        if (v === binding.btnGo) {
            if (isSelected) {
                when (selectedModule) {
                    Module.MVC_KOTLIN -> add(R.id.container, this, UsersFragment())
                    Module.MVVM_KOTLIN -> add(R.id.container, this, UsersFragmentWithViewModel())
                    else -> add(R.id.container, this, UsersFragment())
                }
            } else {
                DialogUtils.showToast(context!!, "Please select one pattern")
            }
        } else if (v === binding.cvMvcKotlin) {
            isSelected = true
            selectedModule = Module.MVC_KOTLIN
            showReveal(binding.cvMvcKotlin, binding.viewBehind)
            binding.cvMvcKotlin.isSelected = true
            binding.cvMvvmKotlin.isSelected = false
        } else if (v === binding.cvMvvmKotlin) {
            isSelected = true
            selectedModule = Module.MVVM_KOTLIN
            showReveal(binding.cvMvvmKotlin, binding.viewBehind)
            binding.cvMvcKotlin.isSelected = false
            binding.cvMvvmKotlin.isSelected = true
        }

    }

    private fun showReveal(centerView: View, viewToReveal: View) {
        // get the center for the clipping circle
        val cx = (centerView.x + centerView.width / 2).toInt()
        val cy = (centerView.y + centerView.height / 2).toInt()
        // get the final radius for the clipping circle
        val finalRadius = Math.max(Math.hypot(cx.toDouble(), cy.toDouble()).toFloat(), 1500f)

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(viewToReveal, cx, cy, centerView.width / 1.8f, finalRadius)
        anim.setDuration(700).interpolator = AccelerateInterpolator()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                viewToReveal.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animator: Animator) {
                viewToReveal.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        // make the view visible and start the animation
        viewToReveal.visibility = View.VISIBLE
        // start animation
        anim.start()
    }
}
