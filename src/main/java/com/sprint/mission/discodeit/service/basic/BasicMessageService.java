package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentDeleteDto;
import com.sprint.mission.discodeit.service.dto.messagedto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentService binaryContentService;


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

        List<UUID> binaryContentUuidList = new ArrayList<>();
        for (Path path : messageCreateDto.attachmentPath()) {
            BinaryContentCreateDto binaryContentCreateDto = new BinaryContentCreateDto(path);
            BinaryContent binaryContent = binaryContentService.create(binaryContentCreateDto);
            if (binaryContent == null) {
                binaryContentUuidList.add(null);
            } else{
                binaryContentUuidList.add(binaryContent.getId());
            }
        }

        Message messages = new Message(messageCreateDto.message(), messageCreateDto.channelId(), messageCreateDto.senderId());
        messages.updateMessage(messageCreateDto.message(), binaryContentUuidList);
        messageRepository.save(messages);

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
    public List<MessageFindAllByChannelIdResponseDto> findAllByChannelId(MessageFindAllByChannelIdRequestDto messageFindAllByChannelIdRequestDto) {
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

        List<UUID> binaryContentUuidList = new ArrayList<>();
        for (Path path : messageUpdateDto.attachmentPath()) {
            BinaryContentCreateDto binaryContentCreateDto = new BinaryContentCreateDto(path);
            BinaryContent binaryContent = binaryContentService.create(binaryContentCreateDto);
            if (binaryContent == null) {
                binaryContentUuidList.add(null);
            } else{
                binaryContentUuidList.add(binaryContent.getId());
            }
        }

        for (UUID oldProfiles : message.getAttachmentIds()) {
            BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(oldProfiles);
            binaryContentService.delete(binaryContentDeleteDto);
        }

        message.updateMessage(messageUpdateDto.changeMessage(), binaryContentUuidList);
        return messageRepository.save(message);
    }


    @Override
    public void delete(MessageDeleteDto messageDeleteDto) {
        Message message = messageRepository.load().stream()
                .filter(m -> m.getId().equals(messageDeleteDto.messageId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Message does not exist."));

        for (UUID oldProfiles : message.getAttachmentIds()) {
            BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(oldProfiles);
            binaryContentService.delete(binaryContentDeleteDto);
        }
        messageRepository.remove(message);

    }
}
