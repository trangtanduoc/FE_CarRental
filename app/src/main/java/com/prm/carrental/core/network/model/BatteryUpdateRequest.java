package com.prm.carrental.core.network.model;

import com.google.gson.annotations.SerializedName;

public class BatteryUpdateRequest {
    @SerializedName("batteryLevel")
    private int batteryLevel;

    public BatteryUpdateRequest(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
}
