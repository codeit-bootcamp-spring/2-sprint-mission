package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextMessage implements Message {
    private static int count;
    private final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public final Long createdAt;
    public Long updatedAt;

    protected final String id;
    private String text;

    public TextMessage(String text) {
        this.id = "TM" + count++;
        this.text = text;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public Long getCreatedAt() {
        System.out.println("생성 시각: " + dayTime.format(new Date(createdAt)));
        return createdAt;
    }

    public Long getUpdatedAt() {
        System.out.println("수정 시각: " + dayTime.format(new Date(updatedAt)));
        return updatedAt;
    }

    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return text;
    }
}
