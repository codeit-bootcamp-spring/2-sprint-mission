package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    List<Message> messageList = new ArrayList<>();

    @Override
    public Message save(UUID userUUID ,UUID channelUUID, String content) {
        Message message = new Message(content, userUUID, channelUUID);
        messageList.add(message);
        return message;
    }

    @Override
    public Optional<Message> findMessageById(UUID messageUUID) {
        return messageList.stream()
                .filter(message -> message.getId().equals(messageUUID))
                .findAny();
    }

    @Override
    public List<Message> findAllMessage() {
        return messageList;
    }

    @Override
    public List<Message> findMessageByChannel(UUID channelUUID) {
        return messageList.stream()
                .filter(message -> message.getChannelUUID().equals(channelUUID))
                .collect(Collectors.toList());
    }

    @Override
    public Message updateMessage(UUID messageUUID, String content) {
        return messageList.stream()
                .filter(message -> message.getId().equals(messageUUID))
                .findAny()
                .map(message -> {
                    message.updateContent(content);
                    return message;
                })
                .orElse(null);
    }

    @Override
    public boolean deleteMessageById(UUID messageUUID) {
        return messageList.removeIf(message -> message.getId().equals(messageUUID));
    }
}
