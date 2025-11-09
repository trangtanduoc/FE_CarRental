package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class UserDto {
    @SerializedName("id")
    private String id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;

    @SerializedName("status")
    private String status;

    @SerializedName("isVerified")
    private boolean isVerified;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("driverLicenseNumber")
    private String driverLicenseNumber;

    @SerializedName("idCardNumber")
    private String idCardNumber;

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }
}
