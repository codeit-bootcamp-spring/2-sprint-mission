package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.SaveMessageRequestDto;
import com.sprint.mission.discodeit.dto.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryDataRepository binaryDataRepository;

    @Override
    public void sendMessage(SaveMessageRequestDto saveMessageRequestDto, List<SaveBinaryContentRequestDto> saveBinaryContentRequestDtoList) {
        userRepository.findUserById(saveMessageRequestDto.userId())
                .orElseThrow(NullPointerException::new);

        channelRepository.findChannelById(saveMessageRequestDto.channelId())
                .orElseThrow(NullPointerException::new);

        List<UUID> attachmentList = saveBinaryContentRequestDtoList.stream()
                .map(data -> {
                    String filename = data.fileName();
                    String contentType = data.contentType();
                    BinaryData binaryData = binaryDataRepository.save(new BinaryData(data.fileData()));
                    binaryContentRepository.save(new BinaryContent(binaryData.getId(), filename, contentType));
                    return binaryData.getId();
                })
                .toList();

        Message message = new Message(
                saveMessageRequestDto.content(), saveMessageRequestDto.userId(),
                saveMessageRequestDto.channelId(), attachmentList);
        messageRepository.save(message);
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return messageRepository.findMessageById(messageId)
                .orElseThrow(() -> new NoSuchElementException(messageId.toString() + " not found"));
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAllMessage();
    }

    @Override
    public List<FindMessageByChannelIdResponseDto> findMessageByChannelId(UUID channelUUID) {
        List<Message> channelMessageList = messageRepository.findMessageByChannel(channelUUID);

        return channelMessageList.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map((message) -> {
                    String nickname = userRepository.findUserById(message.getUserUUID())
                            .map(User::getNickname)
                            .orElse("알 수 없음");

                    return new FindMessageByChannelIdResponseDto(
                            message.getId(), nickname,
                            message.getAttachmentList(), message.getContent(),
                            message.getCreatedAt()
                    );
                })
                .toList();
    }

    @Override
    public void updateMessage(UUID messageId, UpdateMessageRequestDto updateMessageRequestDto, List<SaveBinaryContentRequestDto> saveBinaryContentRequestDtoList) {
        Message message = messageRepository.findMessageById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 메세지를 찾을 수 업습니다."));

        List<UUID> attachmentList = saveBinaryContentRequestDtoList.stream()
                .map(data -> {
                    String filename = data.fileName();
                    String contentType = data.contentType();
                    BinaryData binaryData = binaryDataRepository.save(new BinaryData(data.fileData()));
                    binaryContentRepository.save(new BinaryContent(binaryData.getId(), filename, contentType));
                    return binaryData.getId();
                })
                .toList();

        message.updateContent(updateMessageRequestDto.content());
        message.updateAttachmentList(attachmentList);

        messageRepository.save(message);
    }

    @Override
    public void deleteMessageById(UUID messageUUID) {
        Message message = messageRepository.findMessageById(messageUUID)
                .orElseThrow(NullPointerException::new);
        message.getAttachmentList().forEach(binaryContentRepository::delete);
        messageRepository.delete(message.getId());
    }
}
