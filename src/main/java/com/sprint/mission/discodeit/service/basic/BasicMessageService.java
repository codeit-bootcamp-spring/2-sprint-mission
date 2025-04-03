package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BasicBinaryContentService basicBinaryContentService;

  @Override
  public Message create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryRequestList) {
    UUID channelId = createRequest.channelId();
    UUID authorId = createRequest.authorId();
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + "에 해당하는 Channel을 찾을 수 없음");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException(authorId + "에 해당하는 Author를 찾을 수 없음");
    }
    List<UUID> idList = binaryRequestList.stream()
        .map(request ->
            basicBinaryContentService.create(request).getId()).toList();
    Message message = new Message(createRequest.content(), channelId, authorId, idList);
    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + " 에 해당하는 Message를 찾을 수 없음"));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  @Override
  public Message update(UUID id, MessageUpdateRequest updateRequest) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 Message를 찾을 수 없음"));
    message.update(updateRequest.newContent());
    return messageRepository.save(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = find(messageId);
    if (message.getAttachmentIds() != null) {
      message.getAttachmentIds().forEach(basicBinaryContentService::delete);
    }
    messageRepository.deleteById(messageId);
  }
}
