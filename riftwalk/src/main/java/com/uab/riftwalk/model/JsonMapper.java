package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;
import com.uab.riftwalk.winrate.Player;

import java.util.List;

public class JsonMapper {
    public static final String WINNER_KEY = "winner";
    public static final String REGION_KEY = "region";
    public static final String MMR_KEY = "mmr";
    public static final String PATCH_KEY = "patch";
    public static final String GAME_ID_KEY = "game_id";

    @SerializedName(REGION_KEY)
    private String region;
    @SerializedName(MMR_KEY)
    private String mmr;
    @SerializedName(PATCH_KEY)
    private Integer patch;
    @SerializedName(WINNER_KEY)
    private String  winner;
    @SerializedName(GAME_ID_KEY)
    private String gameId;
    @SerializedName("players")
    private List<Player> players;

    public String getRegion() {
        return region;
    }

    public String getMmr() {
        return mmr;
    }

    public Integer getPatch() {
        return patch;
    }

    public String getWinner() {
        return winner;
    }

    public String getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}