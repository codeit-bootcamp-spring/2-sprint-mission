package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  @Transactional
  public Message sendMessage(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) {
    List<BinaryContent> attachmentList = new ArrayList<>();

    User user = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new NoSuchElementException(
            messageCreateRequest.authorId() + "에 해당하는 사용자를 찾을 수 없습니다."));

    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException(
            messageCreateRequest.channelId() + "에 해당하는 채널을 찾을 수 없습니다."));

    if (attachments != null && !attachments.isEmpty()) {
      attachmentList = attachments.stream()
          .filter(file -> !file.isEmpty())
          .map(data -> {
            try {
              String fileName = data.getOriginalFilename();
              String contentType = data.getContentType();
              byte[] bytes = data.getBytes();
              BinaryContent binaryContent = BinaryContent.builder()
                  .fileName(fileName)
                  .contentType(contentType)
                  .bytes(bytes)
                  .size((long) bytes.length)
                  .build();
              return binaryContent;
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          })
          .toList();
    }

    Message message = new Message(
        messageCreateRequest.content(), user,
        channel, attachmentList);
    messageRepository.save(message);
    return message;
  }

  @Override
  public List<Message> findMessageByChannelId(UUID channelUUID) {
    return messageRepository.findByChannelId(channelUUID);
  }

  @Override
  @Transactional
  public Message updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다."));
    message.updateContent(messageUpdateRequest.newContent());
    return message;
  }

  @Override
  @Transactional
  public void deleteMessageById(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다."));
    messageRepository.delete(message);
  }
}
