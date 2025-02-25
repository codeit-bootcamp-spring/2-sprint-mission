package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.LinkedList;
import java.util.List;

public class Repository {
    private final List<User> userList;

    public Repository() {
        this.userList = new LinkedList<>();
    }

    public List<User> getUserList() {
        return userList;
    }
}
