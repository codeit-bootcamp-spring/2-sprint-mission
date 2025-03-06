package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.FileService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileMessageService implements MessageService, FileService<Message> {
    private final Map<UUID, Message> messageMap;
    private static FileMessageService instance;
    private final ChannelService channelService;
    private final UserService userService;

    private FileMessageService(ChannelService channelService, UserService userService) {
        messageMap = new ConcurrentHashMap<>();
        this.channelService = channelService;
        this.userService = userService;
        loadCacheFromFile();
    }

    public static FileMessageService getInstance(ChannelService channelService, UserService userService) {
        if (instance == null) {
            synchronized (FileMessageService.class) {
                if (instance == null) {
                    instance = new FileMessageService(channelService, userService);
                }
            }
        }
        return instance;
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
        messageMap.put(message.getId(), message);
        saveToFile(message);

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = messageMap.get(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return loadAllFromFile().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = messageMap.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);
        messageMap.put(message.getId(), message);
        saveToFile(message);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (messageMap.get(messageId) == null) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messageMap.remove(messageId);
        deleteFile(messageId);
    }

    // ============================== 직렬화 관련 로직 ===================================
    @Override
    public void saveToFile(Message message) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
        Path filePath = directory.resolve(message.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, message);
    }


    @Override
    public List<Message> loadAllFromFile() {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFile(UUID messageId) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
        Path filePath = directory.resolve(messageId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("메시지 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<Message> messages = loadAllFromFile();
        for (Message message : messages) {
            messageMap.put(message.getId(), message);
        }
    }
}
