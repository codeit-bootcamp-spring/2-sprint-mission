package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.Repository.ServerRepository;

import java.util.UUID;

public class Server extends BaseEntity{
    private String name;

    private ServerRepository serverRepository;

    public Server(String name) {
        super();
        this.name = name;
        this.serverRepository = new ServerRepository();
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public ServerRepository getServerRepository() {
        return serverRepository;
    }
}
