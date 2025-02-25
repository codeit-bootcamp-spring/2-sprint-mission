package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;

public interface UserService {
    public abstract Server createServer(String name);
    public abstract Channel createChannel(String name);
    public abstract void printServer();
    public abstract void printChannel();
    public abstract void removeServer(String targetName);
    public abstract void removeChannel(String targetName);
    public abstract void updateServer(String targetName, String replaceName);
    public abstract void updateChannel(String targetName, String replaceName);
}
