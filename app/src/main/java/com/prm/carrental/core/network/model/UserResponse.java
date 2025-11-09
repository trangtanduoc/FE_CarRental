package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("role")
    private String role;

    @SerializedName("driverLicenseNumber") // optional - chỉ thêm nếu backend trả
    private String driverLicenseNumber;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
    public String getDriverLicenseNumber() { return driverLicenseNumber; }

    public String getRole() {
        return role;
    }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDriverLicenseNumber(String driverLicenseNumber) { this.driverLicenseNumber = driverLicenseNumber; }
}
