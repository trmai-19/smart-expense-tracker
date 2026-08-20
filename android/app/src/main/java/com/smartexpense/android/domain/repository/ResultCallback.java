package com.smartexpense.android.domain.repository;

public interface ResultCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
