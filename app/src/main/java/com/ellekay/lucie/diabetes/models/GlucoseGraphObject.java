package com.ellekay.lucie.diabetes.models;

import org.joda.time.DateTime;

/**
 * Created by lucie on 1/26/2018.
 */

public class GlucoseGraphObject {

    private DateTime created;
    private int reading;

    public GlucoseGraphObject(DateTime created, int reading) {
        this.created = created;
        this.reading = reading;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }
}
