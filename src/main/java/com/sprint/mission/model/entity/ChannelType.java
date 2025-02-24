package com.sprint.mission.model.entity;

public enum ChannelType {
    VOICE_CHANNEL("음성방"),
    TEXT_CHANNEL("채팅방");

    private final String description;

    ChannelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
