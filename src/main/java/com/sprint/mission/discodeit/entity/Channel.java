package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Common {

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
}
