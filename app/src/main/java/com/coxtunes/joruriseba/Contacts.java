package com.coxtunes.joruriseba;

public class Contacts {
    private String id,name, area_chamber_shop, phone, service_name;


    public Contacts(String id, String name, String area_chamber_shop, String phone, String service_name) {
        this.id = id;
        this.name = name;
        this.area_chamber_shop = area_chamber_shop;
        this.phone = phone;
        this.service_name = service_name;
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

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }



}
