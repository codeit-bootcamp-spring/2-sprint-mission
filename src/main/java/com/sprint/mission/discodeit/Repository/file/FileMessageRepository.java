package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.Message.MessageUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileMessageRepository implements MessageRepository {
    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser");

    public FileMessageRepository() {
        loadMessageList();
    }

    private void init() {
        Path directory = path.getParent();
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadMessageList() {
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Map<UUID, List<Message>> list = (Map<UUID, List<Message>>) ois.readObject();
                messageList = list;

                System.out.println("메시지 리스트 로드 완료: " + path);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("메시지 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void saveMessageList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(messageList);

        } catch (IOException e) {
            System.out.println("메시지 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        init();
        try {
            Files.deleteIfExists(path);
            messageList = new ConcurrentHashMap<>();
        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public void save(Channel channel, Message message) {
        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
        messages.add(message);
        saveMessageList();
    }

    @Override
    public Message find(UUID messageId) {
        Message message = messageList.values().stream().flatMap(List::stream)
                .filter(s -> s.getMessageId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> CommonExceptions.MESSAGE_NOT_FOUND);
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (messageList.isEmpty()) {
            throw CommonExceptions.EMPTY_MESSAGE_LIST;
        }
        List<Message> messages = messageList.get(channelId);

        if (messages.isEmpty()) {
            throw CommonExceptions.EMPTY_MESSAGE_LIST;
        }
        return messages;
    }

    @Override
    public UUID update(Message message, MessageUpdateDTO messageUpdateDTO) {
        if (messageUpdateDTO.replaceText() != null) {
            message.setText(messageUpdateDTO.replaceText());
        }
        if (messageUpdateDTO.replaceId() != null) {
            message.setMessageId(messageUpdateDTO.replaceId());
        }
        saveMessageList();
        return message.getMessageId();
    }

    @Override
    public void remove(Channel channel, Message message) {
        List<Message> messages = messageList.get(channel.getChannelId());
        messages.remove(message);
        saveMessageList();
    }
}
