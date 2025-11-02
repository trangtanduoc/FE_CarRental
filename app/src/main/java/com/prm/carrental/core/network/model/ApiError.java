package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single error returned by the backend envelope.
 */
public class ApiError {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
