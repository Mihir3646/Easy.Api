package com.hlabexmaples.easyapi.data.easyapi.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelError {
    @SerializedName("Status")
    @Expose
    private ModelStatus status;

    public ModelStatus getStatus() {
        return status;
    }

    public void setStatus(ModelStatus mStatus) {
        status = mStatus;
    }
}