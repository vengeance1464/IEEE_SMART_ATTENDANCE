package com.ieeepec.smart.attendance;

public class User {

    private String name,college,sid,status;

    public User(String name,String college,String sid,String status)
    {

        this.name=name;
        this.college=college;
        this.sid=sid;
        this.status=status;
    }
    public String getName() {
        return name;
    }

    public String getCollege() {
        return college;
    }

    public String getSid() {
        return sid;
    }

    public String getStatus() {
        return status;
    }
}
