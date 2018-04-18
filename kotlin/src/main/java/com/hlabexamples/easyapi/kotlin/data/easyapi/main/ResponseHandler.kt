package com.hlabexamples.easyapi.kotlin.data.easyapi.main

interface ResponseHandler<in E> {
    fun onResponse(response: E, isSuccess: Boolean, successMessage: String)
}