package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static final FileMessageService instance = new FileMessageService(FileUserService.getInstance(), FileChannelService.getInstance());
    private static final String FILE_PATH = "message.ser";
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    private FileMessageService(UserService userService, ChannelService channelService) {
        this.data = loadData();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static FileMessageService getInstance() {
        return instance;
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, Message> loadData() {
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

    @Override
    public Message createMessage(UUID userId, UUID channelId, String text) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }
        if (!userService.getUserById(userId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId);
        }
        if (!channelService.getChannelById(channelId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId);
        }
        Message message = new Message(userId, channelId, text);
        data.put(message.getId(), message);
        saveData();
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> getAllMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UUID messageId, String newText) {
        Message message = data.get(messageId);
        if (message != null) {
            long currentTime = System.currentTimeMillis();
            message.update(newText, currentTime);
            saveData();
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
        saveData();
    }
}
