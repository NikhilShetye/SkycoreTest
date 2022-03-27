package com.artium.app.extensions

import com.artium.app.network.Receptor
import com.artium.app.network.Status

fun <T> Receptor<T>.onSuccess(callback: Receptor<T>.() -> Unit): Receptor<T> {
    return invokeCallback(callback, Status.SUCCESS)
}

fun <T> Receptor<T>.onFailure(callback: Receptor<T>.() -> Unit): Receptor<T> {
    return invokeCallback(callback, Status.ERROR)
}

fun <T> Receptor<T>.onException(callback: Receptor<T>.() -> Unit): Receptor<T> {
    return invokeCallback(callback, Status.EXCEPTION)
}

private fun <T> Receptor<T>.invokeCallback(
    callback: Receptor<T>.() -> Unit,
    `when`: Status
): Receptor<T> {
    if (getStatus() == `when`) {
        callback.invoke(this)
    }
    return this
}