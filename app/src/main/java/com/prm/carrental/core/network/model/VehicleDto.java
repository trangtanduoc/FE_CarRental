package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class VehicleDto {

    @SerializedName("id")
    private String id;

    @SerializedName("plateNumber")
    private String plateNumber;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("batteryLevel")
    private int batteryLevel;

    @SerializedName("stationId")
    private String stationId;

    @SerializedName("stationName")
    private String stationName;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("ImageUrl")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
