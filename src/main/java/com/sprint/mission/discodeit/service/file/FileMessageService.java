package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileMessageService implements MessageService {

    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
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
        saveToFile(message);

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = loadOneFromFile(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return loadAllFromFile().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = loadOneFromFile(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (loadOneFromFile(messageId) == null) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
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
    public Message loadOneFromFile(UUID messageId) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
        return SerializationUtil.reverseOneSerialization(directory,messageId);
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
}
