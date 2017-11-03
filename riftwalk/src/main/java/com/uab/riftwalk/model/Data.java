package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("damageEvents")
    private List<DamageEvent> damageEvents;
    @SerializedName("buffGainedEvents")
    private List<BuffEvent> buffGainedEvents;
    @SerializedName("buffLostEvents")
    private List<BuffEvent> buffLostEvents;

    public List<DamageEvent> getDamageEvents() {
        return damageEvents;
    }

    public List<BuffEvent> getBuffGainedEvents() {
        return buffGainedEvents;
    }

    public List<BuffEvent> getBuffLostEvents() {
        return buffLostEvents;
    }
}
