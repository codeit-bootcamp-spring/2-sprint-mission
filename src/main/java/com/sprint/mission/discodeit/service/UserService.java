package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public abstract Server createServer(String name);

    public abstract void addServer(UUID userId, String name);

    public abstract void addServer(UUID userId, Server server);

    public abstract Server getServer(UUID userId, String name);

    public abstract void printServer(UUID userId);

    public abstract void printServer(List<Server> list);

    public abstract boolean removeServer(UUID userId, String targetName);

    public abstract boolean updateServer(UUID userId, String targetName, String replaceName);

    public abstract Channel createChannel(String name);

    public abstract void addChannel(UUID userId, Channel Channel);

    public abstract void printChannel(UUID id);

    public abstract boolean removeChannel(String targetName);

    public abstract boolean updateChannel(String targetName, String replaceName);
}
