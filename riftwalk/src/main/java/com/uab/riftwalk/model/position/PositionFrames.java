package com.uab.riftwalk.model.position;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PositionFrames {
    @SerializedName("time")
    private int time;
    @SerializedName("playerPositions")
    private List<PUnit> playerPositions;

    public int getTime() {
        return time;
    }

    public List<PUnit> getPlayerPositions() {
        return playerPositions;
    }
}
