package com.sprint.sprint1.mission.model.entity;

public enum ChannelTypes {
    VOICE_CHANNEL("음성방"),
    TEXT_CHANNEL("채팅방");

    private final String description;

    ChannelTypes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
