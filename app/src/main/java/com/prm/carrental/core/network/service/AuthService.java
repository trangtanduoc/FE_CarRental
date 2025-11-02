package com.prm.carrental.core.network.service;

import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.AuthResponse;
import com.prm.carrental.core.network.model.LoginRequest;
import com.prm.carrental.core.network.model.RegisterRequest;
import com.prm.carrental.core.network.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<ApiResponse<UserResponse>> register(@Body RegisterRequest request);
}
