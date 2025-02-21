package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    Map<UUID, Message> messages = new HashMap<UUID, Message>();

    @Override
    public void createMessage(String userName, String channelName, String content) {
        Message message = new Message(userName, channelName, content);
        messages.put(message.getUuid(), message);
    }

    @Override
    public Message getMessageById(UUID messageId) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지ID 입니다.");
        }
        return messages.get(messageId);
    }

    @Override
    public List<Message> getMessagesByUserAndChannel(String userName, String channelName) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSender().equals(userName) && message.getChannel().equals(channelName)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getChannelMessages(String channelName) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getChannel().equals(channelName)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getUserMessages(String userName) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSender().equals(userName)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지ID 입니다.");
        }
        Message message = messages.get(messageId);
        message.updateContent(newContent);
        messages.put(messageId, message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지ID 입니다.");
        }
        messages.remove(messageId);
    }
}
