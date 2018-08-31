package com.vlad.lesson12_maskaikin.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BridgeResponse {

    @SerializedName("objects")
    private List<Bridge> bridges = new ArrayList<>();

    public List<Bridge> getBridges() {
        return bridges;
    }
}
