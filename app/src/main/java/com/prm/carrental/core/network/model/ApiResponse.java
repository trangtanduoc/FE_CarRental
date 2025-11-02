package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Generic envelope used by the backend for commands and queries.
 */
public class ApiResponse<T> {

    @SerializedName("isSuccess")
    private boolean success;

    @SerializedName(value = "data", alternate = { "value" })
    private T value;

    @SerializedName("errors")
    private List<ApiError> errors;

    public boolean isSuccess() {
        return success;
    }

    public T getValue() {
        return value;
    }

    public List<ApiError> getErrors() {
        return errors == null ? Collections.emptyList() : errors;
    }
}
