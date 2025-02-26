package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {

    // 필드 선언
    private String channelName;
    private String description;

    // 생선자 선언
    public Channel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
    }
    public Channel(String channelName) {
        this.channelName = channelName;
    }

    // getter
    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }


    // 채널 업데이트 메소드 선언
    public void updateChannel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        super.update();
    }

    public boolean equals(Object object){
        if (object instanceof Channel channel) {
            return channel.getChannelName().equals(this.getChannelName());
        }
        return false;
    }

    @Override
    public String toString() {
        return "채널 이름: " + channelName + "\n채널 설명: " + description +
                "\n사용자 ID: " + this.getId() +
                "\n생성 시간: " + this.getCreatedAtFormatted() +
                "\n업데이트 시간: " + this.getupdatedAttFormatted();
    }
}
