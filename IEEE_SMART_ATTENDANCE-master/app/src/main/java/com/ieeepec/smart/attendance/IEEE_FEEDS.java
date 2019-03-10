package com.ieeepec.smart.attendance;

public class IEEE_FEEDS {
    String date;
    String name;
    String description;

    public IEEE_FEEDS(String date, String n, String d) {
        this.date = date;
        this.name = n;
        this.description = d;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
