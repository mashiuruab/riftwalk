package com.uab.riftwalk.model;

import com.google.gson.annotations.SerializedName;

public class DataJsonMapper extends JsonMapper {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}
