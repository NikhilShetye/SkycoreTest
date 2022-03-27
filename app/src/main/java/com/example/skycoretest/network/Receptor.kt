/*
 * **************************************************************************
 * Copyright (C) Verizon Wireless, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * **************************************************************************
 */

package com.artium.app.network

/**
 * Created by Saurabh.Jain on 08-06-2020.
 */
const val DEFAULT_ERROR_CODE = -1

class Receptor<T>(builder: Builder<T>) {

    private var status: Status
    private var response: T? = null
    private var throwable: Throwable? = null
    private var errorMsg: String? = null
    private var errorCode: Int = DEFAULT_ERROR_CODE

    fun getResponse(): T? {
        return response
    }

    fun getStatus(): Status {
        return status
    }

    fun getThrowable(): Throwable? {
        return throwable
    }

    fun getErrorMsg(): String? {
        return errorMsg
    }

    fun getErrorCode(): Int {
        return errorCode
    }

    override fun toString(): String {
        return "Receptor(status=$status, response=$response, throwable=$throwable," +
                " errorMsg=$errorMsg, errorCode=$errorCode)"
    }

    init {
        status = builder.status
        response = builder.response
        throwable = builder.throwable
        errorCode = builder.errorCode
        errorMsg = builder.errorMsg
    }

    class Builder<T>() {
        lateinit var status: Status
        var response: T? = null
        var throwable: Throwable? = null
        var errorMsg: String? = null
        var errorCode: Int = DEFAULT_ERROR_CODE

        fun build(): Receptor<T> {
            return Receptor(this)
        }

        /**
         * Create New Receptor from previous one with alteration
         * @param anotherReceptor Receptor<T>
         * @constructor
         */
        constructor(anotherReceptor: Receptor<T>) : this() {
            status = anotherReceptor.status
            response = anotherReceptor.response
            throwable = anotherReceptor.throwable
            errorCode = anotherReceptor.errorCode
            errorMsg = anotherReceptor.errorMsg
        }

        fun copy(builder: Builder<T>) {
            status = builder.status
            response = builder.response
            throwable = builder.throwable
            errorCode = builder.errorCode
            errorMsg = builder.errorMsg
        }

        override fun toString(): String {
            return "Builder(status=$status, response=$response, throwable=$throwable, " +
                    "errorMsg=$errorMsg, errorCode=$errorCode)"
        }
    }
}

/**
 * To define Receptor Status either API Success, or failed or any exception occurred.
 */
enum class Status {
    SUCCESS,
    EXCEPTION,
    ERROR
}
