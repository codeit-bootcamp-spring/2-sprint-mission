package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.message.ChannelMessage;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.message.PrivateMessage;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private UserService userService;
    private ChannelService channelService;

    private final Map<UUID, Message> messages;
    private final Map<UUID, List<UUID>> messageIdsBySenderId;
    private final Map<UUID, List<UUID>> channelMessageIdsByChannelId;
    private final Map<UUID, List<UUID>> privateMessageIdsByReceiverId;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        messages = new HashMap<>();
        messageIdsBySenderId = new HashMap<>();
        channelMessageIdsByChannelId = new HashMap<>();
        privateMessageIdsByReceiverId = new HashMap<>();
    }

    @Override
    public PrivateMessage sendPrivateMessage(UUID senderId, String content, UUID receiverId) {
        userService.validateUserId(senderId);
        userService.validateUserId(receiverId);
        PrivateMessage privateMessage = new PrivateMessage(senderId, content, receiverId);

        messages.put(privateMessage.getId(), privateMessage);
        storeMessage(messageIdsBySenderId, senderId, privateMessage.getId());
        storeMessage(privateMessageIdsByReceiverId, receiverId, privateMessage.getId());

        return privateMessage;
    }

    @Override
    public ChannelMessage sendChannelMessage(UUID senderId, String content, UUID channelId) {
        channelService.validateChannelId(channelId);
        userService.validateUserId(senderId);
        if (!channelService.isChannelMember(channelId, senderId)) {
            return null;
        }
        ChannelMessage channelMessage = new ChannelMessage(senderId, content, channelId);

        messages.put(channelMessage.getId(), channelMessage);
        storeMessage(messageIdsBySenderId, senderId, channelMessage.getId());
        storeMessage(channelMessageIdsByChannelId, channelId, channelMessage.getId());
        return channelMessage;
    }

    private void storeMessage (Map<UUID, List<UUID>> messageMap, UUID key, UUID messageId) {
        if (!messageMap.containsKey(key)) {
            messageMap.put(key, new ArrayList<>());
        }

        messageMap.get(key).add(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public List<Message> getMessagesBySenderId(UUID senderId) {
        userService.validateUserId(senderId);
        return getMessagesById(messageIdsBySenderId, senderId);
    }

    @Override
    public List<ChannelMessage> getChannelMessagesByChannelId(UUID channelId) {
        channelService.validateChannelId(channelId);
        return getMessagesById(channelMessageIdsByChannelId, channelId).stream()
                .map(message -> (ChannelMessage) message)
                .toList();

    }

    @Override
    public List<PrivateMessage> getPrivateMessagesByReceiverId(UUID receiverId) {
        userService.validateUserId(receiverId);
        return getMessagesById(privateMessageIdsByReceiverId, receiverId).stream()
                .map(message -> (PrivateMessage) message)
                .toList();
    }

    private List<Message> getMessagesById(Map<UUID, List<UUID>> messageMap, UUID keyId) {
        List<UUID> messageIds = messageMap.get(keyId);
        if (messageIds == null) {
            return List.of();
        }

        return messageIds.stream()
                .map(messages::get)
                .toList();
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

        messageIdsBySenderId.get(message.getSenderId()).remove(messageId);

        if (message instanceof PrivateMessage) {
            privateMessageIdsByReceiverId.get(((PrivateMessage) message).getReceiverId()).remove(messageId);
        } else if (message instanceof ChannelMessage) {
            channelMessageIdsByChannelId.get(((ChannelMessage) message).getChannelId()).remove(messageId);
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
