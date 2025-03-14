package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;


public interface ChannelService {
    /**
     * 저장소를 초기화합니다.
     *
     * @param adminAuth 권한 여부
     * */
    void reset(boolean adminAuth);

    Message write(String creatorId, String channelId, String text);

    //단건 조회
    Message getMessage(String serverId,String channelId, String messageId);

    //출력
    void printMessage(String serverId, String channelId);

    //삭제
    boolean removeMessage(String serverId,String channelId, String messageId);

    //업데이트
    boolean updateMessage(String serverId,String channelId, String messageId, String replaceText);

}
