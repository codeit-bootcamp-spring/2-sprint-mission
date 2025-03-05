package com.sprint.mission.discodeit.entity.Container;

import java.util.UUID;

public class Channel extends Container {

    public Channel(String name) {
        super(name);
    }

    public Channel(UUID id, Long createdAt, String name) {
        super(id, createdAt, name);
    }
}
