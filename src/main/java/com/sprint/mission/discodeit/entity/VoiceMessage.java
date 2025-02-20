package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VoiceMessage implements Message{
    private static int count;
    private final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public final Long createdAt;
    public Long updatedAt;

    protected final String id;
    private String voice;


    public VoiceMessage( ) {
        this.id = "TM" + count++;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public String getVoice() {
        return voice;
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
        return voice;
    }
}
