package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity{
    private String name;

    public Message(String str) {
        super();
        this.name = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String str) {
        this.name = str;
    }



}
