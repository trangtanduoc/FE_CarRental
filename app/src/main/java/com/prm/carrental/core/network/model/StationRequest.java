package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class StationRequest {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public StationRequest(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
