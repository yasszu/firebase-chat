package com.example.firebasechat.helper

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.squareup.picasso.Picasso

object ImageViewHelper {

    fun ImageView.loadImage(url: String?, @DrawableRes defaultDrawable: Int) {
        if (url == null || url.isBlank()) {
            Picasso.with(context)
                    .load(defaultDrawable)
                    .error(defaultDrawable)
                    .into(this)
        } else {
            Picasso.with(context)
                    .load(url)
                    .error(defaultDrawable)
                    .into(this)
        }
    }

    fun ImageView.loadCircleImage(url: String?, @DrawableRes defaultDrawable: Int) {
        if (url == null || url.isBlank()) {
            Picasso.with(context)
                    .load(defaultDrawable)
                    .error(defaultDrawable)
                    .transform(CircleTransform())
                    .into(this)
        } else {
            Picasso.with(context)
                    .load(url)
                    .error(defaultDrawable)
                    .transform(CircleTransform())
                    .into(this)
        }
    }

}