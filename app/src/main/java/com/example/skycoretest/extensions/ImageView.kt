package com.example.skycoretest.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.skycoretest.R

fun AppCompatImageView.loadGif(
    @RawRes @DrawableRes resId: Int,
    loopCount: Int = GifDrawable.LOOP_FOREVER,
    onGifCompleted: () -> Unit = {}
) {
    Glide.with(this)
        .asGif()
        .load(resId)
        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
        .listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?,
                target: Target<GifDrawable>?, isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?,
                dataSource: DataSource?, isFirstResource: Boolean
            ): Boolean {
                resource?.setLoopCount(loopCount)
                resource?.registerAnimationCallback(object :
                    Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        onGifCompleted()
                    }
                })
                return false
            }

        }).into(this)
}


fun ImageView.loadProfilePhoto(
    url: String? = null,
) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)

    this.setBackgroundColor(
        ContextCompat.getColor(
            context,
            android.R.color.transparent
        )
    )
}


