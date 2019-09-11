package com.coxtunes.joruriseba;

import android.os.Parcel;
import android.os.Parcelable;

public class Contacts {
    private String id,name, area_chamber_shop, phone, table;

    public Contacts(String id, String name, String area_chamber_shop, String phone, String table) {
        this.id = id;
        this.name = name;
        this.area_chamber_shop = area_chamber_shop;
        this.phone = phone;
        this.table = table;
    }

    public Contacts() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea_chamber_shop() {
        return area_chamber_shop;
    }

    public void setArea_chamber_shop(String area_chamber_shop) {
        this.area_chamber_shop = area_chamber_shop;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

}
