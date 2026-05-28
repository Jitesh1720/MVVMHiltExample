package com.example.demokotlinapp.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.demokotlinapp.R

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadCircular(url: String) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.avatar_placeholder)
        .error(R.drawable.avatar_placeholder)
        .circleCrop()
        .into(this)
}
