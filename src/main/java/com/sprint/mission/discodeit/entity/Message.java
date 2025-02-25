package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message extends BaseEntity{
    private String str;


    public Message(String str) {
        super();
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }



}
