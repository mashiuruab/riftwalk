package com.uab.riftwalk.winrate.prmcalculation;

import com.google.gson.annotations.SerializedName;

public class DataModel {
    @SerializedName("unitName")
    private String unitName;
    @SerializedName("totalWin")
    private Integer totalWin;
    @SerializedName("totalParticipation")
    private Integer totalParticipation;
    @SerializedName("winRate")
    private Double winRate;

    public String getUnitName() {
        return unitName;
    }

    public Integer getTotalWin() {
        return totalWin;
    }

    public Integer getTotalParticipation() {
        return totalParticipation;
    }

    public Double getWinRate() {
        return winRate;
    }
}
