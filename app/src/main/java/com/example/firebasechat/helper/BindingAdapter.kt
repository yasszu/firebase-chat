package com.example.firebasechat.helper

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

object BindingAdapter {

    @BindingAdapter("imageUrl")
    fun ImageView.loadImage(url: String) {
        Picasso.with(context).load(url).into(this)
    }

}