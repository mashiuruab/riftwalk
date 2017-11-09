package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;
import com.uab.riftwalk.model.position.PositionFrames;

import java.util.List;

public class Data {
    @SerializedName("damageEvents")
    private List<DamageEvent> damageEvents;
    @SerializedName("buffGainedEvents")
    private List<BuffEvent> buffGainedEvents;
    @SerializedName("buffLostEvents")
    private List<BuffEvent> buffLostEvents;
    @SerializedName("goldEarnedEvents")
    private List<EarnedEvent> goldEarnedEvents;
    @SerializedName("experienceEarnedEvents")
    private List<EarnedEvent> experienceEarnedEvents;
    @SerializedName("positionFrames")
    private List<PositionFrames> positionFrames;

    public List<DamageEvent> getDamageEvents() {
        return damageEvents;
    }

    public List<BuffEvent> getBuffGainedEvents() {
        return buffGainedEvents;
    }

    public List<BuffEvent> getBuffLostEvents() {
        return buffLostEvents;
    }

    public List<EarnedEvent> getGoldEarnedEvents() {
        return goldEarnedEvents;
    }

    public List<EarnedEvent> getExperienceEarnedEvents() {
        return experienceEarnedEvents;
    }

    public List<PositionFrames> getPositionFrames() {
        return positionFrames;
    }
}
