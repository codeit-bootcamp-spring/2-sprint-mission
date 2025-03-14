package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;


public interface ChannelService {

    void reset(boolean adminAuth);

    Message write(String creatorId, String channelId, String text);

    Message getMessage(String serverId,String channelId, String messageId);

    void printMessage(String serverId, String channelId);

    boolean removeMessage(String serverId,String channelId, String messageId);

    boolean updateMessage(String serverId,String channelId, String messageId, String replaceText);

}
