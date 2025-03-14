package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileMessageService implements MessageService {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");

    private final List<Message> messagesData;
    private final UserService userService;

    public FileMessageService(UserService userService) {
        messagesData = new ArrayList<>();
        this.userService = userService;
    }


    // 메시지 생성
    @Override
    public Message create(String message, UUID channelId, UUID senderId) {
        Message messages = new Message(message, channelId, senderId);
        if (!validateMessage(channelId)) {
            return null;
        }
        messagesData.add(messages);
        save(messages);
        return messages;
    }

    private boolean validateMessage(UUID messageId) {
        User user = userService.getUser(messageId);
        if (user != null && user.getId().equals(messageId)) {
            return true;
        }
        System.out.println("등록 된 사용자가 없습니다.");
        return false;
    }

    private void save(Message message) {
        init();
        Path path = directory.resolve(message.getId() + ".ser");
        saveToFile(path, message);
    }

    private void init() {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(Path path, Message message) {
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 메시지 단일 조회
    @Override
    public Message getMessage(UUID messageId) {
        Optional<Message> message = find(messageId).stream().findAny();
        return message.orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
    }

    private List<Message> find(UUID messageId) {
        return load().stream()
                .filter(message -> message.getId().equals(messageId))
                .toList();
    }

    private List<Message> load() {
        if (Files.exists(directory)) {
            try (Stream<Path> path = Files.list(directory)) {
                return path
                        .map(this::loadFromFile)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private Message loadFromFile(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // 메시지 전체 조회
    @Override
    public List<Message> getAllMessage() {
        List<Message> messageList = load();
        if (messageList.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        return messageList;
    }


    // 메시지 수정
    @Override
    public Message update(UUID messageId, String changeMessage) {
        List<Message> messageList = find(messageId);
        Message messages = messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findAny()
                .orElse(null);
        if (messages == null) {
            System.out.println("메시지가 존재하지 않습니다.");
            return null;
        } else {
            messages.updateMessage(changeMessage);
            save(messages);
        }
        return messages;
    }


    // 메시지 삭제
    @Override
    public void delete(UUID messageId) {
        List<Message> messageList = find(messageId);
        Message messages = messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findAny()
                .orElse(null);
        try {
            if (messages != null && Files.exists(directory.resolve(messages.getId() + ".ser"))) {
                Files.delete(directory.resolve(directory.resolve(messages.getId() + ".ser")));
            } else {
                System.out.println("메시지가 존재하지 않습니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}