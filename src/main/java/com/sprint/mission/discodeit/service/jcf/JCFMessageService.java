package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.PrivateMessage;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;

    private Map<UUID, Message> messages;
    private Map<UUID, List<UUID>> messageIdsBySender;
    private Map<UUID, List<UUID>> channelMessageIdsByChannel;
    private Map<UUID, List<UUID>> privateMessageIdsByReceiver;

    private JCFMessageService() {
        messages = new HashMap<>();
        messageIdsBySender = new HashMap<>();
        channelMessageIdsByChannel = new HashMap<>();
        privateMessageIdsByReceiver = new HashMap<>();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    @Override
    public PrivateMessage sendPrivateMessage(UUID senderId, String content, UUID receiverId) {
        PrivateMessage privateMessage = new PrivateMessage(senderId, content, receiverId);

        messages.put(privateMessage.getId(), privateMessage);
        storeMessage(messageIdsBySender, senderId, privateMessage.getId());
        storeMessage(privateMessageIdsByReceiver, receiverId, privateMessage.getId());

        return privateMessage;
    }

    @Override
    public ChannelMessage sendChannelMessage(UUID senderId, String content, UUID channelId) {
        if (!JCFChannelService.getInstance().isChannelMember(channelId, senderId)) {
            return null;
        }
        ChannelMessage channelMessage = new ChannelMessage(senderId, content, channelId);

        messages.put(channelMessage.getId(), channelMessage);
        storeMessage(messageIdsBySender, senderId, channelMessage.getId());
        storeMessage(channelMessageIdsByChannel, channelId, channelMessage.getId());
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
        return getMessagesById(messageIdsBySender, senderId);
    }

    @Override
    public List<ChannelMessage> getChannelMessagesByChannelId(UUID channelId) {
        return getMessagesById(channelMessageIdsByChannel, channelId).stream()
                .map(message -> (ChannelMessage) message)
                .toList();

    }

    @Override
    public List<PrivateMessage> getPrivateMessagesByReceiverId(UUID receiverId) {
        return getMessagesById(privateMessageIdsByReceiver, receiverId).stream()
                .map(message -> (PrivateMessage) message)
                .toList();
    }

    private List<Message> getMessagesById(Map<UUID, List<UUID>> messageMap, UUID keyId) {
        List<UUID> messageIds = messageMap.get(keyId);
        if (messageIds == null) {
            return List.of();
        }

        return messageIds.stream()
                .map(messageId -> messages.get(messageId))
                .toList();
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public Message updateMessage(UUID messageId, String content) {
        return null;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        return false;
    }
}
