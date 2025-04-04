package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentDeleteDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageFindRequestDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageFindResponseDto;
import com.sprint.mission.discodeit.service.dto.messagedto.MessageUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;


    @Override
    public Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtoList) {
        List<User> userList = userRepository.load();
        List<Channel> channelList = channelRepository.load();
        Optional<User> user = userList.stream()
                .filter(u -> u.getId().equals(messageCreateDto.authorId()))
                .findAny();
        Optional<Channel> channel = channelList.stream()
                .filter(c -> c.getId().equals(messageCreateDto.channelId()))
                .findAny();
        if (user.isEmpty()) {
            throw new NotFoundException("User not found.");
        }
        if (channel.isEmpty()) {
            throw new NotFoundException("Channel not found.");
        }

        List<UUID> attachmentIds = binaryContentCreateDtoList.stream()
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        Message messages = new Message(messageCreateDto.content(), messageCreateDto.channelId(), messageCreateDto.authorId(), attachmentIds);
        messageRepository.save(messages);

        return messages;
    }


    @Override
    public MessageFindResponseDto find(MessageFindRequestDto messageFindRequestDto) {
        Message matchingMessage = messageRepository.loadToId(messageFindRequestDto.messageId())
                .orElseThrow(() -> new NotFoundException("Message does not exist."));
        return MessageFindResponseDto.fromMessage(matchingMessage);
    }


    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.load().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();

    }


    @Override
    public Message update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        Message matchingMessage = messageRepository.loadToId(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        matchingMessage.updateMessage(messageUpdateDto.newContent());
        return messageRepository.save(matchingMessage);
    }


    @Override
    public void delete(UUID messageId) {
        Message matchingMessage = messageRepository.loadToId(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        matchingMessage.getAttachmentIds().forEach(binaryContentService::delete);
        messageRepository.remove(matchingMessage);

    }
}
