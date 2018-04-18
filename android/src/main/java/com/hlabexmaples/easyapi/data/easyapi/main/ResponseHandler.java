package com.hlabexmaples.easyapi.data.easyapi.main;

public interface ResponseHandler<E> {
    void onResponse(E response, boolean isSuccess, String successMessage);
}