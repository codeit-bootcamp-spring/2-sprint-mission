package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Server extends Domain {
    private static int count;
    private List<Channel> channels = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public Server() {
        this("S"+count++,null);
    }

    public Server(String id, String name) {
        super(id, name);
    }
}
