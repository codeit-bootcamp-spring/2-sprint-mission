package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String str;
    public Message(String id, String name, String str) {
        super(id, name);
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}
