package com.uab.riftwalk.winrate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonMapper {
    @SerializedName("winner")
    private String  winner;
    @SerializedName("players")
    private List<Player> players;

    public String getWinner() {
        return winner;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
