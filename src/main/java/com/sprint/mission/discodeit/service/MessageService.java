package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto createMessage(CreateMessageRequest request);

  void addAttachment(UUID messageId, BinaryContent attachment);

  MessageDto findMessageById(UUID messageId); // 메세지 조회

  List<MessageDto> findMessagesByUserAndChannel(UUID senderId,
      UUID channelId); // 특정 유저가 특정 채널에서 작성한 메세지 확인

  List<MessageDto> findAllByChannelId(UUID channelId); // 채널 내 모든 메세지 확인

  //PageResponse<MessageDto> findMessagesByPage(UUID channelId, int page);

  PageResponse<MessageDto> findMessagesByCursor(UUID channelId, Instant cursor);

  List<MessageDto> findAllByUserId(UUID authorId); // 유저의 모든 메세지 확인

  MessageDto updateMessage(UUID messageId, UpdateMessageRequest request); // 메세지 내용 수정

  void deleteMessage(UUID messageId); // 메세지 삭제

  void validateMessageExists(UUID messageId); // 메세지 존재 확인
}
