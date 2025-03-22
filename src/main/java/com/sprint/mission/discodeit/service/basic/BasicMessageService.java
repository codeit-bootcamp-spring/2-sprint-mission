package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageFindDTO;
import com.sprint.mission.discodeit.dto.create.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.create.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
    public Message create(CreateMessageRequestDTO messageWriteDTO, List<Optional<CreateBinaryContentRequestDTO>> binaryContentDTOs) {
        User user = userRepository.findById(messageWriteDTO.userId());
        Channel channel = channelRepository.find(messageWriteDTO.channelId());

        Message message = new Message(user.getId(), user.getName(), channel.getChannelId(), messageWriteDTO.text());

        List<UUID> binaryContentIdList = makeBinaryContent(binaryContentDTOs);
        message.setAttachmentIds(binaryContentIdList);

        messageRepository.save(channel, message);
        return message;
    }

    @Override
    public MessageFindDTO find(UUID messageId) {
        Message message = messageRepository.find(messageId);
        MessageFindDTO messageFindDTO = new MessageFindDTO(message.getUserName(), message.text, message.createdAt);

        return messageFindDTO;
    }

    @Override
    public List<MessageFindDTO> findAllByChannelId(UUID channelId) {
        List<Message> list = messageRepository.findAllByChannelId(channelId);
        List<MessageFindDTO> messageFindDTOS = list.stream().map(message -> new MessageFindDTO(message.getUserName(), message.text, message.createdAt)).toList();
        return messageFindDTOS;
    }

    @Override
    public void print(UUID channelId) {
        Channel channel = channelRepository.find(channelId);
        System.out.println(channel.getName());
        List<Message> messages = messageRepository.findAllByChannelId(channel.getChannelId());
        for (Message message : messages) {
            System.out.println(message.getUserName() + " : " + message.getText());
        }
    }

    @CustomLogging
    @Override
    public void delete(UUID messageId) {

        Message message = messageRepository.find(messageId);

        messageRepository.remove(messageId);
        if (message.getAttachmentIds().isEmpty() == false) {
            List<UUID> attachmentIds = message.getAttachmentIds();
            for (UUID attachmentId : attachmentIds) {
                binaryContentRepository.delete(attachmentId);
            }
        }
    }

    @CustomLogging
    @Override
    public UUID update(UUID messageId, UpdateMessageDTO updateMessageDTO) {

        Message message = messageRepository.find(messageId);

        Message update = messageRepository.update(message, updateMessageDTO);
        return update.getMessageId();
    }

    private List<UUID> makeBinaryContent(List<Optional<CreateBinaryContentRequestDTO>> binaryContentDTOs) {
        List<UUID> collect = binaryContentDTOs.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::saveBinaryContent)
                .collect(Collectors.toList());
        return collect;
    }

    private UUID saveBinaryContent(CreateBinaryContentRequestDTO binaryContentCreateDTO) {
        BinaryContent content = new BinaryContent(
                binaryContentCreateDTO.fileName(),
                (long) binaryContentCreateDTO.bytes().length,
                binaryContentCreateDTO.contentType(),
                binaryContentCreateDTO.bytes()
        );
        binaryContentRepository.save(content);
        return content.getId();
    }
}
