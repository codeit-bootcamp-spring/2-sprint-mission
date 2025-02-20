package com.sprint.mission.discodeit.entity;

public class TextMessage extends Domain implements Message{
    private static int count;
    private String text;

    public TextMessage() {
        this("TM" + count++, null);
    }

    public TextMessage(String id, String name) {
        super(id, name);
    }

    public String getText() {
        return text;
    }
}
