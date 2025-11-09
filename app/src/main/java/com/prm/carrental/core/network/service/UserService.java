package com.prm.carrental.core.network.service;

import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.UserDto;
import com.prm.carrental.core.network.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("users")
    Call<ApiResponse<PagedResponse<UserDto>>> getUsers(
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize
    );

    @GET("users")
    Call<ApiResponse<PagedResponse<UserDto>>> getRenters(
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize,
            @Query("role") String role
    );

    @GET("users/{id}")
    Call<ApiResponse<UserDto>> getUserById(
            @Path("id") String id
    );

    @DELETE("users/{id}")
    Call<ApiResponse<Boolean>> deleteUserById(
            @Path("id") String id
    );
}
