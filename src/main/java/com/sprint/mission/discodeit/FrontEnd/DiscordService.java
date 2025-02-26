package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.entity.User;

public interface DiscordService {
    public User create();

    public User create(String name);
    public void add(User user);
    public void remove();
    public void print();
    public void update();
}
