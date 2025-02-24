package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {
    private String channelName;
    private List<User> users;
    private int userCount;

    public Channel(String channelName) {
        super();
        this.channelName = channelName;
        this.users = new ArrayList<>();
        this.userCount = 0;
    }

    public String getChannelName() {
        return channelName;
    }

    public List<User> getUsers() {
        return users;
    }

    public int getUserCount() {
        return userCount;
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            userCount++;
            System.out.println("사용자 " + user.getUserName() + "이(가) '" + channelName + "'에 추가되었습니다.");
        } else {
            System.out.println("사용자 " + user.getUserName() + "은(는) 이미 '" + channelName + "'에 존재합니다.");
        }
    }

    public void removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            userCount--;
            System.out.println("사용자 " + user.getUserName() + "이(가) '" + channelName + "'에서 삭제되었습니다.");
        } else {
            System.out.println("사용자 " + user.getUserName() + "은(는) '" + channelName + "'에 존재하지 않습니다.");
        }
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }
}
