package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    void createMessage(String userName, String channelName, String content); //메세지 생성

    Message getMessageById(UUID uuid);
    List<Message> getMessagesByUserAndChannel(String userName, String channelName); //특정 유저가 특정 채널에서 작성한 메세지 확인
    List<Message> getChannelMessages(String channelName); //채널내 모든 메세지 확인
    List<Message> getUserMessages(String userName); //유저의 모든 메세지 확인

    void updateMessage(UUID messageId, String newContent); //메세지 내용 수정

    void deleteMessage(UUID messageId); //메세지 삭제
}
