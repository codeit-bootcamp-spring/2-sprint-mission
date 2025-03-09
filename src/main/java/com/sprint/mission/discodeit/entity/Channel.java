package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Channel extends CommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public Channel() {
        super();
    }

    @Override
    public String toString() {
        return "[CHANNEL " +super.toString();

    }
}
