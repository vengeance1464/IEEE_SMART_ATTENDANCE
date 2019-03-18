package com.ieeepec.smart.attendance;

import java.util.ArrayList;

public class IEEE_EVENT {

    private String date;
   // private String event;
    private int id;
    private String name;
    private String qr;
    private ArrayList<String>reg;
//    private int image;



  /*  public void setId(int id) {
        this.id = id;
    }
*/
    public String getDate() {
        return date;
    }

  /*  public void setDate(String date) {
        this.date = date;
    }
*/
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getQr() {
        return qr;
    }

    public ArrayList<String> getReg() {
        return reg;
    }
    /*public void setEvent(String event) {
        this.event = event;
    }
*/
  /*  public int getImage() {
        return image;
    }
*/
  /*  public void setImage(int image) {
        this.image = image;
    }
    */
}

