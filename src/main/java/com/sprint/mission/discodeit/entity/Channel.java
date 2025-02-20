package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel extends Common {

    // 해당 채널의 주인
    private final User user;
    // 채널명
    private String channelName;
    // 해당 채널의 유저리스트
    private List<User> users = new ArrayList<>();

    public Channel(User user, String channelName) {
        // super() 하지 않아도 기본 생성자는 컴파일러가 자동으로 삽입
        this.user = user;
        this.channelName = channelName;
    }

    public User getUser() {return user; }

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

    // 유저 추가 - 유저가 채널에 들어올 경우
    public void addUsers(User user) {
        users.add(user);
    }

    // 유저 삭제 - 유저가 채널에 나갈 경우
    public void removeUsers(User user) {
        users.remove(user);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "user=" + user +
                ", channelName='" + channelName + '\'' +
                ", users=" + users +
                '}';
    }
}
