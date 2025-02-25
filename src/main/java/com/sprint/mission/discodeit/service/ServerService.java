package com.sprint.mission.discodeit.service;

public interface ServerService {
    public abstract void print();
    public abstract void addChannel(String name);
    public abstract void update(String targetName, String replaceName);
    public abstract void remove(String targetName);
}
