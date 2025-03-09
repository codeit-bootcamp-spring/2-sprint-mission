package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static FileMessageService instance;
    private static final Path directoryPath = Paths.get("data/messages");

    private FileUserService userservice;
    private FileChannelService channelservice;

    public static synchronized FileMessageService getInstance(FileUserService userService, FileChannelService channelService) {
        if (instance == null) {
            instance = new FileMessageService(userService, channelService);
        }
        return instance;
    }

    private FileMessageService(FileUserService userservice, FileChannelService channelservice) {
        this.userservice = userservice;
        this.channelservice = channelservice;
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + e.getMessage());
        }
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId) {
        if (!userservice.existUser(userId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다. 메세지를 생성할 수 없습니다.");
        }
        if (!channelservice.existChannel(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널 입니다. 메세지를 생성할 수 없습니다.");
        }
        Message message = new Message(userId, channelId);
        saveMessage(message);
        System.out.println("메세지가 생성되었습니다: \n" + message);
        return message.getId();
    }

    public void saveMessage(Message message) {
        Path filePath = getFilePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("메세지 저장 실패: " + message.getId(), e);
        }
    }

    public Message loadMessage(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메세지 데이터 읽기 실패: " + filePath, e);
        }
    }

    public Path getFilePath(UUID messageId) {
        return directoryPath.resolve("message-" + messageId + ".data");
    }

    @Override
    public void searchMessage(UUID id) {
        Path filePath = getFilePath(id);
        if (!Files.exists(filePath)) {
            System.out.println("조회하신 메세지가 존재하지 않습니다.");
            return;
        }
        Message message = loadMessage(filePath);
        System.out.println("MESSAGE: " + message);
    }

    @Override
    public void searchAllMessages() {
        try {
            Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        Message message = loadMessage(path);
                        System.out.println("MESSAGE: " + message);
                    });
        } catch (IOException e) {
            throw new RuntimeException("메세지 목록 읽기 실패: " + e);
        }
    }

    @Override
    public void updateMessage(UUID id) {
        Path filePath = getFilePath(id);
        if (!Files.exists(filePath)) {
            System.out.println("업데이트할 메세지가 존재하지 않습니다.");
            return;
        }
        Message message = loadMessage(filePath);
        message.updateTime(System.currentTimeMillis());
        saveMessage(message);
        System.out.println(id + " 메세지 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteMessage(UUID id) {
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
            System.out.println(id + " 메세지 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("메세지 삭제 실패: " + id, e);
        }
    }

    public boolean existMessage(UUID id) {
        return Files.exists(getFilePath(id));
    }
}