package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

public class BuffEvent {
    @SerializedName("unitID")
    private Integer unitID;
    @SerializedName("buffName")
    private Double buffName;

    public Integer getUnitID() {
        return unitID;
    }

    public Double getBuffName() {
        return buffName;
    }
}
