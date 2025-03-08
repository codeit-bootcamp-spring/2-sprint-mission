package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileMessageService implements MessageService {

    private Map<UUID, Message> data = new HashMap<>();
    private final AtomicInteger messageNum = new AtomicInteger(1);
    private final UserService userService;
    private final ChannelService channelService;
    private static final String FILE_NAME = "message.sar";

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        loadFromFile();
    }

    @Override
    public UUID create(String content, UUID userKey, UUID channelKey) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("[Error] 내용을 입력해주세요");
        }
        int messageId = messageNum.getAndIncrement();
        Message text = new Message(messageId, content, userKey, channelKey, userService.getUserName(userKey), channelService.getChannelName(channelKey));
        data.put(text.getUuid(), text);
        saveToFile();
        return text.getUuid();
    }

    @Override
    public Message read(UUID channelKey) {
        Message lastMessage = getLastMessage(channelKey);
        if (lastMessage == null) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return lastMessage;
    }

    @Override
    public List<Message> readAll(UUID channelKey) {
        List<Message> allMessage = getAllMessage(channelKey);
        if (allMessage.isEmpty()) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return getAllMessage(channelKey);
    }

    @Override
    public UUID update(int messageId, String content) {
        UUID messageKey = convertToKey(messageId);
        Message message = data.get(messageKey);
        if (message == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }
        if (!content.isEmpty()) {
            message.updateContent(content);
        }
        saveToFile();
        return messageKey;
    }

    @Override
    public void delete(int messageId) {
        UUID messageUuid = convertToKey(messageId);
        Message message = data.get(messageUuid);
        if (message == null) {
            throw new IllegalArgumentException("[Error] 삭제할 메시지가 존재하지 않습니다");
        }
        data.remove(messageUuid);
        saveToFile();
    }

    private Message getLastMessage(UUID channelKey) {
        return data.values().stream()
                .filter(m -> m.getChannelName().equals(channelService.getChannelName(channelKey)))
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
    }

    private List<Message> getAllMessage(UUID channelKey) {
        return data.values().stream()
                .filter(m -> m.getChannelName().equals(channelService.getChannelName(channelKey)))
                .toList();
    }

    private UUID convertToKey(int messageId) {
        return data.values().stream()
                .filter(m -> m.getMessageId() == messageId)
                .map(Message::getUuid)
                .findFirst()
                .orElse(null);
    }

    private void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생 했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("[Info] 데이터 파일이 없습니다. 빈 데이터로 시작합니다.");
            data = new HashMap<>();
            return;
        }

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> rawMap) {
                for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof Message value) {
                        data.put(key, value);
                    }
                }
            }
            System.out.println("[Info] 사용자 데이터를 성공적으로 로드했습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
            data = new HashMap<>();
        }
    }
}
