package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

public class DamageJsonMapper extends JsonMapper {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}
