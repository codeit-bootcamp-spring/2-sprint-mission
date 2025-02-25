package com.sprint.mission.discodeit.entity;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    public List<Server> severList;

    public UserRepository() {
        this.severList = new LinkedList<>();
    }

    public List<Server> getSeverList() {
        return severList;
    }
}
