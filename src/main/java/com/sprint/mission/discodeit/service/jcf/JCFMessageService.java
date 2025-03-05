package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private UserService userService;
    private ChannelService channelService;

    private final Map<UUID, Message> messages;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        messages = new HashMap<>();
    }


    @Override
    public Message sendMessage(UUID senderId, String content, UUID channelId) {
        channelService.validateChannelId(channelId);
        userService.validateUserId(senderId);
        if (!channelService.isChannelMember(channelId, senderId)) {
            return null;
        }
        Message message = new Message(senderId, content, channelId);

        messages.put(message.getId(), message);
        return message;
    }


    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }


    @Override
    public Message getMessageByMessageId(UUID messageId) {
        validateMessageId(messageId);
        return messages.get(messageId);
    }

    @Override
    public Message updateMessage(UUID messageId, String content) {
        validateMessageId(messageId);
        Message message = messages.get(messageId);
        if (message == null) {
            throw new MessageNotFoundException("해당 메세지 없음");
        }
        message.update(content);

        return message;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        validateMessageId(messageId);
        Message message = messages.get(messageId);
        if (message == null) {
            return false;
        }

        messages.remove(messageId);

        return true;
    }

    private void validateMessageId(UUID messageId) {
        if (!messages.containsKey(messageId)) {
            throw new MessageNotFoundException("해당 메세지 없음");
        }
    }
}
