package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

    private final String MESSAGE_FILE;
    private final Map<UUID, Message> messageData;
    private final SaveLoadHandler<Message> saveLoadHandler;

    public FileMessageRepository(@Value("${discodeit.repository.file.message}") String fileName,SaveLoadHandler<Message> saveLoadHandler) {
        MESSAGE_FILE = fileName;
        this.saveLoadHandler = saveLoadHandler;
        messageData = saveLoadHandler.loadData(MESSAGE_FILE);
    }


    @Override
    public Message save(Message message) {
        messageData.put(message.getId(), message);
        saveLoadHandler.saveData(MESSAGE_FILE,messageData);

        return message;
    }

    @Override
    public Message findById(UUID id) {
        return messageData.get(id);
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId)).toList();
        if(messages.isEmpty()){
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        return messages;
}

    @Override
    public List<Message> findByChannel(UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
        if(messages.isEmpty()){
            throw new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId) && m.getChannelId().equals(channelId)).toList();
        if (messages.isEmpty()) {
            throw new NoSuchElementException("유저 " + userId + " 혹은 채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream().toList();
    }

    @Override
    public Message update(UUID id, String newMessage) {
        Message messageNullable = messageData.get(id);
        Message message = Optional.ofNullable(messageNullable).orElseThrow(() -> new NoSuchElementException("메세지 " + id + "가 존재하지 않습니다."));
        message.updateMessage(newMessage);
        saveLoadHandler.saveData(MESSAGE_FILE,messageData);
        return message;
    }

    @Override
    public void delete(UUID id) {
        if(!messageData.containsKey(id)){
            throw new NoSuchElementException("채널 " + id + "가 존재하지 않습니다.");
        }
        messageData.remove(id);
        saveLoadHandler.saveData(MESSAGE_FILE,messageData);
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        messageData.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        messageData.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }


}
