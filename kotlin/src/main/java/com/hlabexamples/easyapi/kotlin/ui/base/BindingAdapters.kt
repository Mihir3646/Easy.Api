package com.hlabexamples.easyapi.kotlin.ui.base

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object BindingAdapters {

    /**
     * To set visibility
     *
     * @param view
     * @param show
     */
    @JvmStatic
    @BindingAdapter("visible")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * To show image from url
     *
     * @param imageView
     * @param imageUrl
     * @param placeholder
     */
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
    fun setImageUrl(imageView: ImageView, imageUrl: String, placeholder: Drawable) {
        if (!TextUtils.isEmpty(imageUrl)) {
            val builder = Glide.with(imageView.context).asBitmap()
            val options = RequestOptions().placeholder(placeholder).error(placeholder).circleCrop()
            builder.apply(options).load(imageUrl).into(imageView)
        }
    }

}