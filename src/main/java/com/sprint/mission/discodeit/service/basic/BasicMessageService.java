package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.messagedto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateDto messageCreateDto) {
        List<User> userList = userRepository.load();
        List<Channel> channelList = channelRepository.load();
        Optional<User> user = userList.stream()
                .filter(u -> u.getId().equals(messageCreateDto.senderId()))
                .findAny();
        Optional<Channel> channel = channelList.stream()
                .filter(c -> c.getId().equals(messageCreateDto.channelId()))
                .findAny();
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        if (channel.isEmpty()) {
            throw new IllegalArgumentException("Channel not found.");
        }
        Message messages = new Message(messageCreateDto.message(), messageCreateDto.channelId(), messageCreateDto.senderId());
        messageRepository.save(messages);

        // 첨부파일을 등록할수 있도록 binarycontent 로직 구현 필요

        return messages;
    }


    @Override
    public MessageFindResponseDto find(MessageFindRequestDto messageFindRequestDto) {
        Message message = messageRepository.load().stream()
                .filter(m -> m.getId().equals(messageFindRequestDto.messageId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Message does not exist."));
        return MessageFindResponseDto.fromMessage(message);
    }


    @Override
    public List<MessageFindAllByChannelIdResponseDto> findallByChannelId(MessageFindAllByChannelIdRequestDto messageFindAllByChannelIdRequestDto) {
        List<Message> messageList = messageRepository.load().stream()
                .filter(m -> m.getChannelId().equals(messageFindAllByChannelIdRequestDto.channelId()))
                .toList();
        return MessageFindAllByChannelIdResponseDto.fromChannel(messageList);
    }


    @Override
    public Message update(MessageUpdateDto messageUpdateDto) {
        Message message = messageRepository.load().stream()
                .filter(m -> m.getId().equals(messageUpdateDto.messageId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Message does not exist."));
        message.updateMessage(messageUpdateDto.changeMessage());
        return messageRepository.save(message);
    }


    @Override
    public void delete(MessageDeleteDto messageDeleteDto) {
        Message message = messageRepository.load().stream()
                .filter(m -> m.getId().equals(messageDeleteDto.messageId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Message does not exist."));
        messageRepository.remove(message);
        // 첨부파일인 binaryContent 삭제하는 로지 구현 필요
    }
}
