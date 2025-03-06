package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {

    private static FileMessageService INSTANCE;
    private final String FILE_PATH = "src/main/resources/messages.dat";
    private final UserService userService;
    private final ChannelService channelService;
    private Map<UUID, Message> messages = new HashMap<>();

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
    public Message createMessage(UUID senderId, UUID channelId, String content) {
        userService.validateUserExists(senderId);
        channelService.validateChannelExists(channelId);

        if(!channelService.findChannelById(channelId).getMembers().contains(senderId)){
            throw new IllegalStateException("유저가 채널에 속해있지 않음.");
        }

        Message message = new Message(senderId, channelId, content);
        channelService.addMessage(channelId, message.getId());
        channelService.updateChannelData();
        messages.put(message.getId(), message);
        saveMessage();

        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        validateMessageExists(messageId);
        return messages.get(messageId);
    }

    @Override
    public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findChannelMessages(UUID channelId) {
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findUserMessages(UUID senderId) {
        return messages.values().stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        validateMessageExists(messageId);
        Message message = messages.get(messageId);
        message.updateContent(newContent);
        saveMessage();
    }

    @Override
    public void deleteMessage(UUID messageId) {
        validateMessageExists(messageId);

        Message message = messages.get(messageId);
        channelService.removeMessage(message.getChannelId(), messageId);
        channelService.updateChannelData();
        messages.remove(messageId);
        saveMessage();
    }

    @Override
    public void validateMessageExists(UUID messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NoSuchElementException("존재하지 않는 메세지입니다.");
        }
    }
}
