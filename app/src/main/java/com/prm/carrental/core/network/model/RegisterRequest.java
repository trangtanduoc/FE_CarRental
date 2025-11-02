package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("fullName")
    private final String fullName;

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    @SerializedName("driverLicenseNumber")
    private final String driverLicenseNumber;

    @SerializedName("idCardNumber")
    private final String idCardNumber;

    public RegisterRequest(
        String fullName,
        String email,
        String password,
        String driverLicenseNumber,
        String idCardNumber
    ) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.driverLicenseNumber = driverLicenseNumber;
        this.idCardNumber = idCardNumber;
    }
}
