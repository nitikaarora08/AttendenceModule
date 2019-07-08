package com.patanjali.attendencemodule.model;

import io.realm.RealmObject;

/**
 * Created by Patanjali on 24-10-2018.
 */

public class LatitudeModel extends RealmObject {

    Double latitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}