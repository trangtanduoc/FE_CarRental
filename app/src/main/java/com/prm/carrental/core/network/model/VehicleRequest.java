package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class VehicleRequest {
    @SerializedName("plateNumber")
    private String plateNumber;

    @SerializedName("type")
    private String type;

    @SerializedName("batteryLevel")
    private int batteryLevel;

    @SerializedName("stationId")
    private String stationId;

    @SerializedName("image")
    private String imageUrl;

    public VehicleRequest(String plateNumber, String type, int batteryLevel, String stationId, String imageUrl) {
        this.plateNumber = plateNumber;
        this.type = type;
        this.batteryLevel = batteryLevel;
        this.stationId = stationId;
        this.imageUrl = imageUrl;
    }
}
