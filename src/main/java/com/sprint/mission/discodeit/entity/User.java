package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    private String name;

    @Serial
    private static final long serialVersionUID = 1L;

    public User(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void update(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
