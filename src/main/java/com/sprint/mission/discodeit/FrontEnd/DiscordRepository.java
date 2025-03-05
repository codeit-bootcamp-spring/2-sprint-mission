package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface DiscordRepository {
    public abstract void register(User user);

    public abstract List<User> getUserList();

    public abstract void updateUserList(List<User> userList);
}
