package com.patanjali.attendencemodule.model;

import io.realm.RealmObject;

/**
 * Created by Patanjali on 24-10-2018.
 */

public class DateTimeModel extends RealmObject {

    String datetime;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}