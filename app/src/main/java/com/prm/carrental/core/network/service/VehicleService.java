package com.prm.carrental.core.network.service;

import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.BatteryUpdateRequest;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.VehicleDto;
import com.prm.carrental.core.network.model.VehicleRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VehicleService {
    @GET("vehicles")
    Call<ApiResponse<PagedResponse<VehicleDto>>> getVehicles(
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize,
            @Query("status") String status
    );
    @GET("vehicles/{id}")
    Call<ApiResponse<VehicleDto>> getVehicleById(
            @Path("id") String vehicleId
    );
    @DELETE("vehicles/{id}")
    Call<ApiResponse<Boolean>> deleteVehicleById(
            @Path("id") String id
    );

//    @POST("vehicles")
//    Call<ApiResponse<VehicleDto>> createVehicle(
//            @Body VehicleRequest vehicle
//    );

    @Multipart
    @POST("vehicles")
    Call<ApiResponse<VehicleDto>> createVehicle(
            @Part("plateNumber") RequestBody plateNumber,
            @Part("type") RequestBody type,
            @Part("batteryLevel") RequestBody batteryLevel,
            @Part("stationId") RequestBody stationId,
            @Part MultipartBody.Part image
    );

    @PATCH("vehicles/{id}/battery")
    Call<ApiResponse<VehicleDto>> updateBatteryLevel(
            @Path("id") String id,
            @Body BatteryUpdateRequest request
    );
}
