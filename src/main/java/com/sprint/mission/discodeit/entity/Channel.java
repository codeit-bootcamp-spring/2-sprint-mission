package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends Common {
    // 채널명
    private String channelName;
    // 해당 채널의 유저리스트
    private List<User> users = new ArrayList<>();

    public Channel(String channelName, List<User> users) {
        // super() 하지 않아도 기본 생성자는 컴파일러가 자동으로 삽입
        this.channelName = channelName;
        this.users = users;
    }

    public String getChannelName() {
        return channelName;
    }

    public List<User> getUsers() {
        return users;
    }

    // 채널명 수정
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    // 유저 수정 - 채널에 유저가 들어가고 나갈 수 있음
    public void updateUsers(List<User> users) {
        this.users = users;
    }
}
