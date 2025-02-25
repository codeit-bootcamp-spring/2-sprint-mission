package com.sprint.mission.discodeit.entity.Container;

import com.sprint.mission.discodeit.entity.BaseEntity;

import java.util.UUID;

public abstract class Container extends BaseEntity {
    private String name;

    public Container(String name) {
        super();
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
