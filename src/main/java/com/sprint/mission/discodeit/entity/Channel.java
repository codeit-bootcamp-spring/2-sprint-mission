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
}
