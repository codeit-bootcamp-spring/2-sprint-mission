package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.entity.User;

public interface DiscordService {
    public User create();

    public User create(String name);

    public void register(User user);

    public User get();

    public User get(User user);

    public User get(String targetName);

    public boolean remove();

    public boolean remove(String targetName);

    public boolean remove(User user);

    public void print();

    public boolean update();

    public boolean update(String targetName);

    public boolean update(User user);

    public boolean update(User user, String replaceName);

    public boolean update(String targetName, String replaceName);
}
