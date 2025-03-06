package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
        update();
    }
}
