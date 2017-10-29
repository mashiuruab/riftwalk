package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("damageEvents")
    private List<DamageEvent> damageEvents;

    public List<DamageEvent> getDamageEvents() {
        return damageEvents;
    }
}
