package com.patanjali.attendencemodule.model;

import io.realm.RealmObject;

/**
 * Created by Patanjali on 24-10-2018.
 */

public class AddressModel extends RealmObject {

    String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}