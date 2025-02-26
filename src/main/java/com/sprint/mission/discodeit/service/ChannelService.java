package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

/** <h3>채널 서비스 </h3><p>
 * 유저는 채널에 메시지를 생성한다. <br>
 * 다른 유저는 메시지를 공유한다.</p>
 */
public interface ChannelService {
    public abstract Message write( UUID channelId);
    public abstract Message write(UUID channelId, String str);

    //단건 조회
    public abstract Message getMessage(UUID channelId, String str);

    //출력
    public abstract void printChannel(UUID channelId);

    public abstract void printChannel(List<Message> list);

    //삭제
    public abstract boolean removeMessage(UUID channelId, String targetName);

    //업데이트
    public abstract boolean updateMessage(UUID channelId, String targetName, String replaceName);

}
