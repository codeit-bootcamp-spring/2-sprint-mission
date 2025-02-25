package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    public abstract void currentHead();
    public abstract void createServer(String name);
    public abstract void createChannel(String name);
    public abstract void printServer();
    public abstract void printChannel();
    public abstract void replaceHead();
    public abstract void removeServer(String targetName);
    public abstract void removeChannel(String targetName);
    public abstract void updateServer(String targetName, String replaceName);
    public abstract void updateChannel(String targetName, String replaceName);
}
