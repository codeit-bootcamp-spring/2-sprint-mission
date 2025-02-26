package com.sprint.mission.discodeit.Entry;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DiscordRepository {
    List<User> userList;

    public DiscordRepository() {
        this.userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }
}
