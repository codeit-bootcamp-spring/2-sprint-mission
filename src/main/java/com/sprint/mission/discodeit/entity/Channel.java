package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Channel extends BaseEntity {

    // 필드 선언
    private final ChannelType type;
    private String name;
    private String description;

    // 생선자 선언
    public Channel(ChannelType type, String channelName, String description) {
        this.type = type;
        this.name = channelName;
        this.description = description;
    }

    // 채널 업데이트 메소드 선언
    public void updateChannel(String channelName, String description) {
        this.name = channelName;
        this.description = description;
        super.update();
        System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", this.name, this.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Channel channel) {
            return channel.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nChannel Type: " + type +
                "\nChannelName: " + name + "\nDescription: " + description +
                "\nChannel ID: " + this.getId() +
                "\nCreatedAt: " + this.getCreatedAt() +
                "\nUpdatedAt: " + this.getUpdatedAt();
    }
}
