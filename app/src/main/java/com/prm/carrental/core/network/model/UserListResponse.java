package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class UserListResponse extends PagedResponse<UserResponse> {
    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("data")
    private PagedResponse<UserDto> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public PagedResponse<UserDto> getData() {
        return data;
    }
}
