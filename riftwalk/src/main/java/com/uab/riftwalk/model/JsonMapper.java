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
    public static final String LENGTH_KEY = "length";
    public static final String CHAMPION_IDS_KEY = "champion_ids";

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
    @SerializedName(LENGTH_KEY)
    private Double length;
    @SerializedName("players")
    private List<Player> players;
    @SerializedName(CHAMPION_IDS_KEY)
    private List<Integer> champion_ids;

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

    public Double getLength() {
        return length;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer> getChampion_ids() {
        return champion_ids;
    }
}
