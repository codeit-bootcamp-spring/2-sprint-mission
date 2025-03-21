package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.requestToService.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.requestToService.MessageWriteDTO;
import com.sprint.mission.discodeit.dto.legacy.message.MessageCRUDDTO;
import com.sprint.mission.discodeit.exception.legacy.NotFoundException;
import com.sprint.mission.discodeit.exception.legacy.NotFoundExceptions;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            messageRepository.reset();
        }
    }

    @CustomLogging
    @Override
    public Message create(MessageWriteDTO messageWriteDTO,List<Optional<BinaryContentCreateDTO>> binaryContentDTOs) {
        UUID userId = UUID.fromString(messageWriteDTO.creatorId());
        UUID channelUUID = UUID.fromString(messageWriteDTO.channelId());

        User user = userRepository.findById(userId);
        Channel channel = channelRepository.find(channelUUID);

        Message message = new Message(userId,user.getName(), channelUUID, messageWriteDTO.text());

        List<UUID> binaryContentIdList = makeBinaryContent(binaryContentDTOs);
        message.setAttachmentIds(binaryContentIdList);

        messageRepository.save(channel, message);
        return message;
    }

    @Override
    public Message find(String messageId) {
        UUID messageUUID = UUID.fromString(messageId);

        Message message = messageRepository.find(messageUUID);

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        List<Message> list = messageRepository.findAllByChannelId(channelUUID);
        return list;
    }

    @Override
    public void print(String channelId) {
        try {
            UUID channelUUID = UUID.fromString(channelId);

            Channel channel = channelRepository.find(channelUUID);
            System.out.println(channel.getName());

            List<Message> messages = messageRepository.findAllByChannelId(channel.getChannelId());
            for (Message message : messages) {
                System.out.println(message.getCreatorName() + " : " + message.getText());
            }
        } catch (NotFoundException e) {
            throw NotFoundExceptions.MESSAGE_NOT_FOUND;
        }

    }
    @CustomLogging
    @Override
    public boolean delete(String messageId) {
        UUID messageUUID = UUID.fromString(messageId);

        Message message = messageRepository.find(messageUUID);

        messageRepository.remove(messageUUID);
        if (message.getAttachmentIds().isEmpty() == false) {
            List<UUID> attachmentIds = message.getAttachmentIds();
            for (UUID attachmentId : attachmentIds) {
                binaryContentRepository.delete(attachmentId);
            }
        }
        return true;
    }

    @CustomLogging
    @Override
    public boolean update(String messageId, MessageCRUDDTO messageCRUDDTO) {
        UUID messageUUID = UUID.fromString(messageId);

        Message message = messageRepository.find(messageUUID);

        messageRepository.update(message, messageCRUDDTO);
        return true;
    }

    private List<UUID> makeBinaryContent(List<Optional<BinaryContentCreateDTO>> binaryContentDTOs) {
        List<UUID> collect = binaryContentDTOs.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::saveBinaryContent)
                .collect(Collectors.toList());
        return collect;
    }

    private UUID saveBinaryContent(BinaryContentCreateDTO binaryContentCreateDTO) {
        BinaryContent content = new BinaryContent(
                binaryContentCreateDTO.fileName(),
                (long)binaryContentCreateDTO.bytes().length,
                binaryContentCreateDTO.contentType(),
                binaryContentCreateDTO.bytes()
        );
        binaryContentRepository.save(content);
        return content.getId();
    }
}
