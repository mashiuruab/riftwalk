package com.uab.riftwalk.helper;

import com.google.gson.annotations.SerializedName;

public class DomainModel {
    @SerializedName("patch")
    private Double patch;
    @SerializedName("mmr")
    private Double mmr;
    @SerializedName("region")
    private Double region;

    public Double getPatch() {
        return patch;
    }

    public Double getMmr() {
        return mmr;
    }

    public Double getRegion() {
        return region;
    }
}
