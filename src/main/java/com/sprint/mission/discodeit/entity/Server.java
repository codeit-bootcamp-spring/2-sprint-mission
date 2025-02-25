package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.Repository.ServerRepository;

public class Server extends BaseEntity{
    private ServerRepository serverRepository;

    public Server(String name) {
        super(name);
        this.serverRepository = new ServerRepository();
    }

    public ServerRepository getServerRepository() {
        return serverRepository;
    }
}
