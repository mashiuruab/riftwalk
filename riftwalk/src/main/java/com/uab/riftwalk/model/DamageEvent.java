package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

public class DamageEvent {
    @SerializedName("time")
    private Double time;
    @SerializedName("receiverUnitID")
    private Integer receiverUnitID;
    @SerializedName("dealerUnitID")
    private Integer dealerUnitID;
    @SerializedName("damage")
    private Double damage;

    public Double getTime() {
        return time;
    }

    public Integer getReceiverUnitID() {
        return receiverUnitID;
    }

    public Integer getDealerUnitID() {
        return dealerUnitID;
    }

    public Double getDamage() {
        return damage;
    }
}
