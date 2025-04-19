package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Channel extends BaseUpdatableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private ChannelType type;

    public Channel(ChannelType channelType, String name, String description) {
        this.name = name;
        this.description = description;
        this.type = channelType;
    }

    public void update(String name, String description) {
        if (this.type == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("Private 파일은 수정할 수 없습니다.");
        }

        if (description != null && !description.equals(this.description)) {
            this.description = description;
        }

        if (name != null && !name.equals(this.name)) {
            this.name = name;
        }
    }
}
