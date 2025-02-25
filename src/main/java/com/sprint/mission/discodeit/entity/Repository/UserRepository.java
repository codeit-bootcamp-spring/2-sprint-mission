package com.sprint.mission.discodeit.entity.Repository;

import com.sprint.mission.discodeit.entity.Server;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private final List<Server> severList;

    public UserRepository() {
        this.severList = new LinkedList<>();
    }

    public List<Server> getSeverList() {
        return severList;
    }
}
