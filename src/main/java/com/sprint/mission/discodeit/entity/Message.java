package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
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
