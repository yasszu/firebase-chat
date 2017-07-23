package com.example.firebasechat.extension

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.example.firebasechat.helper.CircleTransformation
import com.squareup.picasso.Picasso

/**
 * Created by Yasuhiro Suzuki on 2017/07/23.
 */

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
                .transform(CircleTransformation())
                .into(this)
    } else {
        Picasso.with(context)
                .load(url)
                .error(defaultDrawable)
                .transform(CircleTransformation())
                .into(this)
    }
}