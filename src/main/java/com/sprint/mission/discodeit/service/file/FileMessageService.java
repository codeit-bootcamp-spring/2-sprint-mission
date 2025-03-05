package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final String fileName = "message.ser";
    private final Map<UUID, Message> messageMap;
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.messageMap = loadMessageList();
        this.channelService = channelService;
        this.userService = userService;
    }

    public void saveMessageList() {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(messageMap);
        } catch (IOException e) {
            throw new RuntimeException("데이터를 저장하는데 실패했습니다.", e);
        }
    }

    public Map<UUID, Message> loadMessageList() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object messageMap = ois.readObject();
            return (Map<UUID, Message>) messageMap;
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("데이터를 불러오는데 실패했습니다", e);
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        try {
            channelService.findById(channelId);
            userService.findById(userId);
        } catch (NoSuchElementException e) {
            throw e;
        }
        Message message = new Message(content, channelId, userId);
        this.messageMap.put(message.getId(), message);
        saveMessageList();
        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Message findById(UUID messageId) {
        Message message = this.messageMap.get(messageId);
        return Optional.ofNullable(message)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = this.messageMap.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
        message.update(newContent);
        saveMessageList();
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Message removeMessage = this.messageMap.remove(messageId);
        if (removeMessage == null) {
            throw new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId);
        }
    }
}
