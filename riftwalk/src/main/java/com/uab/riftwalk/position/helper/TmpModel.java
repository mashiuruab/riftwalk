package com.uab.riftwalk.position.helper;

import com.google.gson.annotations.SerializedName;

public class TmpModel {
    @SerializedName("start")
    private Double start;
    @SerializedName("middle")
    private Double middle;
    @SerializedName("end")
    private Double end;

    public Double getStart() {
        return start;
    }

    public Double getMiddle() {
        return middle;
    }

    public Double getEnd() {
        return end;
    }
}
