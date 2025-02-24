package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String channel;

    public Channel(String channel) {
        super();
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
    public Channel updateChannel(String channel) {
        if(this.channel.equals(channel)){
            System.out.println("같은 이름으로 변경할 수 없습니다.");
        }
        this.channel = channel;

        return this;
    }
}
