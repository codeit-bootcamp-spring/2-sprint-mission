package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {

    private static FileMessageService INSTANCE;
    private final String FILE_PATH = "src/main/resources/messages.dat";
    private final UserService userService;
    private final ChannelService channelService;
    private Map<UUID, Message> messages = new HashMap<UUID, Message>();

    private FileMessageService(UserService userService, ChannelService channelService) {
        loadMessage();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static synchronized FileMessageService getInstance(UserService userService, ChannelService channelService) {
        if (INSTANCE == null) {
            INSTANCE = new FileMessageService(userService, channelService);
        }
        return INSTANCE;
    }

    private void saveMessage(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장 중 오류 발생", e);
        }
    }
    
    private void loadMessage(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (EOFException e) {
            System.out.println("⚠ messages.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 로드 중 오류 발생", e);
        }
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        userService.validateUserExists(userId);
        channelService.validateChannelExists(channelId);

        Message message = new Message(userId, channelId, content);
        channelService.addMessageToChannel(channelId, message.getId());
        channelService.updataChannelData();
        messages.put(message.getId(), message);
        saveMessage();

        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        validateMessage(messageId);
        return messages.get(messageId);
    }

    @Override
    public List<Message> getMessagesByUserAndChannel(UUID userId, UUID channelId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSenderId().equals(userId) && message.getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getUserMessages(UUID userId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSenderId().equals(userId)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        validateMessage(messageId);
        Message message = messages.get(messageId);
        message.updateContent(newContent);
        saveMessage();
    }

    @Override
    public void deleteMessage(UUID messageId) {
        validateMessage(messageId);

        Message message = messages.get(messageId);
        channelService.removeMessageFromChannel(message.getChannelId(), messageId);
        channelService.updataChannelData();
        messages.remove(messageId);
        saveMessage();
    }

    @Override
    public void validateMessage(UUID messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NoSuchElementException("존재하지 않는 메세지입니다.");
        }
    }
}
