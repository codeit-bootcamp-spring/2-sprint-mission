package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.entity.User;

public interface DiscordService {
    public abstract User create();

    public abstract User create(String name);

    public abstract User get();

    public abstract User get(User user);

    public abstract User get(String targetName);

    public abstract boolean remove();

    public abstract boolean remove(String targetName);

    public abstract boolean remove(User user);

    public abstract void print();

    public abstract boolean update();

    public abstract boolean update(String targetName);

    public abstract boolean update(User user);

    public abstract boolean update(User user, String replaceName);

    public abstract boolean update(String targetName, String replaceName);
}
