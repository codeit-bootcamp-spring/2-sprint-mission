package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Channel extends SharedEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String introduction;
    private ChannelType type;

    public Channel(ChannelType type, String name, String introduction) {
        this.type = type;
        this.name = name;
        this.introduction = introduction;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return String.format("\n key= %s\n name= %s\n introduction= %s\n createdAt= %s\n updatedAt= %s\n",
                uuid, name, introduction, createdAt, updatedAt);
    }
}
