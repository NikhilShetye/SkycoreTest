package com.artium.app.extensions

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun String.toForegroundColorSpannedString(
    context: Context,
    textToHighlight: String,
    @ColorRes highlightColor: Int
) =
    SpannableString(this).setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, highlightColor)),
        this.indexOf(textToHighlight),
        this.length,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )