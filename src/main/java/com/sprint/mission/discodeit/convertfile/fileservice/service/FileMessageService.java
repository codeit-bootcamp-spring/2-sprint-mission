package com.sprint.mission.discodeit.convertfile.fileservice.service;

import com.sprint.mission.discodeit.convertfile.entity.Message;
import com.sprint.mission.discodeit.convertfile.fileservice.ChannelService;
import com.sprint.mission.discodeit.convertfile.fileservice.MessageService;
import com.sprint.mission.discodeit.convertfile.fileservice.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private Map<UUID, Message> data;
    private final String FILE_PATH = "messages.dat";
    //
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
        loadFromFile();
    }

    // 파일에서 데이터 로드
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                this.data = (HashMap<UUID, Message>) readObject;
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우 무시하고 새로운 Map 사용
            this.data = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading message data: " + e.getMessage());
            this.data = new HashMap<>();
        }
    }

    // 파일에 데이터 저장
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Error saving message data: " + e.getMessage());
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try {
            channelService.find(channelId);
            userService.find(authorId);
        } catch (NoSuchElementException e) {
            throw e;
        }

        Message message = new Message(content, channelId, authorId);
        this.data.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = this.data.get(messageId);
        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = this.data.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);
        saveToFile();
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!this.data.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        this.data.remove(messageId);
        saveToFile();
    }
}