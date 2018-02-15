package com.ellekay.lucie.diabetes.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lucie on 2/14/2018.
 */

public class PatientsResponse {
    @SerializedName("results")
    private List<Patient> results;

    public List<Patient> getResults() {
        return results;
    }

    public void setResults(List<Patient> results) {
        this.results = results;
    }
}
