package com.prm.carrental.core.network.service;

import com.prm.carrental.core.network.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface UserService {
    // Model request khi update thông tin


    @GET("users/{id}")
    Call<UserResponse> getUserById(
            @Header("Authorization") String token,
            @Path("id") String userId
    );

    @PUT("users/{id}")
    Call<UserResponse> updateUserProfile(
            @Path("id") String userId,
            @Body UpdateUserRequest body
    );

    // lớp request
    class UpdateUserRequest {
        private String fullName;
        private String driverLicenseNumber;

        public UpdateUserRequest(String fullName, String driverLicenseNumber) {
            this.fullName = fullName;
            this.driverLicenseNumber = driverLicenseNumber;
        }

        public String getFullName() {
            return fullName;
        }

        public String getDriverLicenseNumber() {
            return driverLicenseNumber;
        }
    }
}
