package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)
    Message createMessage(MessageCreateDto dto);

    Message getMessageById(UUID messageId); // 메세지 조회

    List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId); // 특정 유저가 특정 채널에서 작성한 메세지 확인

    List<BinaryContent> findAttachmentsById(UUID messageId);

    List<Message> findallByChannelId(UUID channelId); // 채널 내 모든 메세지 확인

    List<Message> findallByUserId(UUID senderId); // 유저의 모든 메세지 확인

    void updateMessage(MessageUpdateDto dto); // 메세지 내용 수정

    void deleteMessage(UUID messageId); // 메세지 삭제

    void validateMessageExists(UUID messageId); // 메세지 존재 확인
}
