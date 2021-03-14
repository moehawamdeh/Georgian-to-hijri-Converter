package com.ts.grp.g2hdateconverter.repository.apiclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

/**
 * StateData
 * Description: This class is a wrapper for data/error to be observed via LiveData
 * @param <T>
 */
public class DataState<T> {

    @NonNull
    private Status status;

    @Nullable
    private T data;

    @Nullable
    private Errors error;
    private Date serverDateTime;


    public DataState() {
        this.status = Status.CREATED;
        this.data = null;
        this.error = null;
    }

    public DataState<T> loading() {
        this.status = Status.LOADING;
        this.data = null;
        this.error = null;
        return this;
    }

    public DataState<T> success(@NonNull T data) {
        this.status = Status.SUCCESS;
        this.data = data;
        this.error = null;
        return this;
    }
    public DataState<T> success() {
        this.status = Status.SUCCESS;
        this.error = null;
        this.data=null;
        return this;
    }

    public DataState<T> error(@NonNull Errors error) {
        this.status = Status.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    public DataState<T> complete() {
        this.status = Status.COMPLETE;
        return this;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Errors getError() {
        return error;
    }

    public DataState<T> setServerDateTime(Date date) {
        this.serverDateTime=date;
        return this;
    }

    public Date getServerDateTime() {
        return serverDateTime;
    }

    public enum Status {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE
    }
    public enum Errors {
        NETWORK_ERROR,CONVERTER_ERROR,API_NO_RESPONSE, TRANSACTION_FAILED, BAD_REQUEST
    }
}