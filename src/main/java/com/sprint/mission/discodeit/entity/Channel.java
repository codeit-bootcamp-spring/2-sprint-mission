package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Channel extends BaseEntity {

    // 필드 선언
    private final ChannelType type;
    private String channelName;
    private String description;

    // 생선자 선언
    public Channel(ChannelType type, String channelName, String description) {
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }

    // 채널 업데이트 메소드 선언
    public void updateChannel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        super.update();
        System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", this.channelName, this.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelName, description);
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
                "\nChannelName: " + channelName + "\nDescription: " + description +
                "\nChannel ID: " + this.getId() +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted();
    }
}
