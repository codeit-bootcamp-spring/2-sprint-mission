package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.SaveMessageParamDto;
import com.sprint.mission.discodeit.dto.UpdateMessageParamDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void sendMessage(SaveMessageParamDto saveMessageParamDto) {
        userRepository.findUserById(saveMessageParamDto.userUUID())
                .orElseThrow(NullPointerException::new);

        channelRepository.findChannelById(saveMessageParamDto.channelUUID())
                .orElseThrow(NullPointerException::new);

        Message message = new Message(
                saveMessageParamDto.content(), saveMessageParamDto.userUUID(),
                saveMessageParamDto.channelUUID(), saveMessageParamDto.attachmentList());
        messageRepository.save(message);
    }

    @Override
    public Message findMessageById(UUID messageUUID) {
        return messageRepository.findMessageById(messageUUID)
                .orElseThrow(() -> new NoSuchElementException(messageUUID.toString() + " not found"));
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
    public void updateMessage(UpdateMessageParamDto updateMessageParamDto) {
        Message message = messageRepository.findMessageById(updateMessageParamDto.messageUUID())
                .orElseThrow(() -> new NoSuchElementException(updateMessageParamDto.messageUUID().toString() + "를 찾을 수 업습니다."));
        message.updateContent(updateMessageParamDto.content());
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
