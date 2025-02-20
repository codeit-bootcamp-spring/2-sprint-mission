package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.Scanner;

public interface ChannelService {
    Channel register(Message message);

    Channel update(Channel channel);

    Channel testRegister(Message message);

}
