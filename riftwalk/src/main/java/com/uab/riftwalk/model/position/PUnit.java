package com.uab.riftwalk.model.position;

import com.google.gson.annotations.SerializedName;

public class PUnit {
    @SerializedName("unitID")
    private int unitID;
    @SerializedName("position")
    private PositionXY position;

    public int getUnitID() {
        return unitID;
    }

    public PositionXY getPosition() {
        return position;
    }
}
