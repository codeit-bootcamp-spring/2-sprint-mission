package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    //
    private final ChannelService channelService;
    private final UserService userService;
    private final BasicBinaryContentService basicBinaryContentService;

    public JCFMessageService(ChannelService channelService, UserService userService,
                             BasicBinaryContentService basicBinaryContentService) {
        this.data = new HashMap<>();
        this.channelService = channelService;
        this.userService = userService;
        this.basicBinaryContentService = basicBinaryContentService;
    }

    @Override
    public Message create(MessageCreateRequest createRequest,
                          List<BinaryContentCreateRequest> binaryContentRequestList) {
        channelService.find(createRequest.channelId());
        userService.find(createRequest.authorId());
        List<UUID> idList = binaryContentRequestList.stream()
                .map(request ->
                        basicBinaryContentService.create(request).getId()).toList();
        Message message = new Message(createRequest.content(), createRequest.channelId(),
                createRequest.authorId(), idList);
        this.data.put(message.getId(), message);

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = this.data.get(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId)).toList();
    }

    @Override
    public Message update(MessageUpdateRequest updateRequest) {
        Message messageNullable = this.data.get(updateRequest.id());
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + updateRequest.id() + " not found"));
        message.update(updateRequest.newContent());

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Message message = find(messageId);
        if (message.getAttachmentIds() != null) {
            message.getAttachmentIds().forEach(basicBinaryContentService::delete);
        }
        this.data.remove(messageId);
    }

    private List<Message> findAll() {
        return this.data.values().stream().toList();
    }
}
