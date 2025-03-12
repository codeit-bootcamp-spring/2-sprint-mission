package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;


public interface ChannelService {
    public abstract Message write( UUID channelId);
    public abstract Message write(UUID channelId, String str);

    //단건 조회
    public abstract Message getMessage(UUID channelId, String str);

    //출력
    public abstract void printChannel(UUID channelId);

    //삭제
    public abstract boolean removeMessage(UUID channelId, String targetName);

    //업데이트
    public abstract boolean updateMessage(UUID channelId, String targetName, String replaceName);

}
