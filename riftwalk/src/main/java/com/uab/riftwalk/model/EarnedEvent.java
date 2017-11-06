package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

public class EarnedEvent {
    @SerializedName("unitID")
    private Integer unitID;
    @SerializedName("time")
    private Double time;

    public Integer getUnitID() {
        return unitID;
    }

    public Double getTime() {
        return time;
    }
}
