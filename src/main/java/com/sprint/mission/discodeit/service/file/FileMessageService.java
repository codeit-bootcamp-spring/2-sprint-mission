package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "messaages.ser";
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try{
            channelService.find(channelId);             //채널 존재 확인하는 비즈니스 로직. JCF구현체와 동일
            userService.find(authorId);                 //사용자 존재 확인하는 비즈니스 로직. JCF구현체와 동일
        } catch (NoSuchElementException e){
            throw e;
        }

        Message message = new Message(content, channelId, authorId);    //메세지 생성하는 비즈니스 로직. JCF 동일.
        Map<UUID, Message> messages = loadMessages();
        messages.put(message.getId(), message);
        saveMessages(messages);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Map<UUID, Message> messages = loadMessages();
        Message message = messages.get(messageId);
        if (message == null) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(loadMessages().values());
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Map<UUID, Message> messages = loadMessages();
        Message message = messages.get(messageId);
        if (message == null) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        message.update(newContent);
        saveMessages(messages);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> messages = loadMessages();
        if (!messages.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messages.remove(messageId);
        saveMessages(messages);
    }

    private Map<UUID, Message> loadMessages() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveMessages(Map<UUID, Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);      //파일에 데이터 쓰기. Map을 사용해서 직접 데이터를 조작한 JCF구현체와 차이점.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
