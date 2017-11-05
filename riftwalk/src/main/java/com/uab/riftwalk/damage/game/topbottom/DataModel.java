package com.uab.riftwalk.damage.game.topbottom;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataModel {
    @SerializedName("doneList")
    private List<Double> doneList;
    @SerializedName("healList")
    private List<Double> healList;
    @SerializedName("takenList")
    private List<Double> takenList;

    public List<Double> getDoneList() {
        return doneList;
    }

    public List<Double> getHealList() {
        return healList;
    }

    public List<Double> getTakenList() {
        return takenList;
    }
}
