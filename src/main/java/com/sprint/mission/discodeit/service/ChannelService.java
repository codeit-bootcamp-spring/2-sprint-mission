package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import java.util.*;

public interface ChannelService {
    void createChannel(Channel channel);
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();
    void updateChannel(UUID id, String name);
    void deleteChannel(UUID id);
}