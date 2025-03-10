package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private Map<UUID, Message> messageData;
    private static final String MESSAGE_FILE_PATH = "messages.ser";

    public FileMessageService() {
        dataLoad();
    }

    private void dataLoad() {
        File file = new File(MESSAGE_FILE_PATH);
        if (!file.exists()) {
            messageData = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(MESSAGE_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messageData = (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    private void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MESSAGE_FILE_PATH))) {
            oos.writeObject(messageData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 저장할 수 없습니다.");
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        this.messageData.put(message.getId(), message);

        dataSave();
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = this.messageData.get(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return this.messageData.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = this.messageData.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);

        dataSave();
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!this.messageData.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        this.messageData.remove(messageId);

        dataSave();
    }
}
