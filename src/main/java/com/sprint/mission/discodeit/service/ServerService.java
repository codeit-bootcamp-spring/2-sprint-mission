package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.composit.Channel;
import com.sprint.mission.discodeit.entity.User;

public interface ServerService {
    void initServer();

    void addChannel(Channel channel);
    Channel searchChannel();
    void updateChannel(Channel channel);
    void removeChannel();
    void printAllChannels();

    void addUser(User user);
    User searchUser();
    void updateUser(User user);
    void removeUser();
    void printAllUsers();

}
