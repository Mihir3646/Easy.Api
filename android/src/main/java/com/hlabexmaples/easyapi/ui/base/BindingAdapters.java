package com.hlabexmaples.easyapi.ui.base;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {

  /**
   * To set visibility
   */
  @BindingAdapter("visible")
  public static void showHide(View view, boolean show) {
    view.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  /**
   * To show image from url
   */
  @BindingAdapter(value = { "imageUrl", "placeholder" }, requireAll = false)
  public static void setImageUrl(ImageView imageView, String imageUrl, Drawable placeholder) {
    if (!TextUtils.isEmpty(imageUrl)) {
      RequestBuilder<Bitmap> builder = Glide.with(imageView.getContext()).asBitmap();
      RequestOptions options =
          new RequestOptions().placeholder(placeholder).error(placeholder).circleCrop();
      builder.apply(options).load(imageUrl).into(imageView);
    }
  }
}