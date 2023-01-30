package com.gogoship.gogoship.model;

public class TicketPositionModel {

    public int pos;
    public boolean check=false;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public TicketPositionModel(int pos, boolean check) {
        this.pos = pos;
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
