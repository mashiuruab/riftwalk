package com.uab.riftwalk.winrate;

import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("name")
    private String name;
    @SerializedName("teamID")
    private String teamID;
    @SerializedName("championID")
    private String championID;

    public String getName() {
        return name;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getChampionID() {
        return championID;
    }
}
