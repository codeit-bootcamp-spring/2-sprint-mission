package com.sprint.mission.discodeit.entity;

public class PublicChannel extends Channel {
    public PublicChannel(String name, String description) {
        super(ChannelType.PUBLIC, name, description);
    }
}