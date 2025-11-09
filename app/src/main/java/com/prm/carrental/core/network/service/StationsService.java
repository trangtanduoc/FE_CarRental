package com.prm.carrental.core.network.service;

import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.StationDto;
import com.prm.carrental.core.network.model.StationRequest;
import com.prm.carrental.core.network.model.VehicleDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StationsService {

    @GET("stations")
    Call<ApiResponse<PagedResponse<StationDto>>> getStations(
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );

    @GET("stations/{id}")
    Call<ApiResponse<StationDto>> getStationById(@Path("id") String stationId);

    @GET("stations/{id}/vehicles")
    Call<ApiResponse<PagedResponse<VehicleDto>>> getVehiclesInStation(
        @Path("id") String stationId,
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );

    @DELETE("stations/{id}")
    Call<ApiResponse<Boolean>> deleteStationById(
        @Path("id") String id
    );

    @POST("stations")
    Call<ApiResponse<StationDto>> createStation(
        @Body StationRequest station
    );

    @PUT("stations/{id}")
    Call<ApiResponse<Boolean>> updateStation(
        @Path("id") String id,
        @Body StationRequest station
    );
}
