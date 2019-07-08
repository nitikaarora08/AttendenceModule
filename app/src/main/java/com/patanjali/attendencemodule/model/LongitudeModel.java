package com.patanjali.attendencemodule.model;

import io.realm.RealmObject;

/**
 * Created by Patanjali on 24-10-2018.
 */

public class LongitudeModel extends RealmObject{

    Double longitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
