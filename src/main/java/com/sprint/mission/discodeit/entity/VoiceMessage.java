package com.sprint.mission.discodeit.entity;

public class VoiceMessage extends Domain implements Message{
    private static int count;
    private String text;

    public VoiceMessage() {
        this("VM" + count++, null);
    }

    public VoiceMessage(String id, String name) {
        super(id, name);
    }

    public String getText() {
        return text;
    }
}
