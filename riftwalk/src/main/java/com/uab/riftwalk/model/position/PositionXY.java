package com.uab.riftwalk.model.position;

import com.google.gson.annotations.SerializedName;

public class PositionXY {
    @SerializedName("x")
    private int x;
    @SerializedName("y")
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
