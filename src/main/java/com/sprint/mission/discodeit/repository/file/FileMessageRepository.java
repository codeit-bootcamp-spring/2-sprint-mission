package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private static final Path directoryPath = Paths.get("data/messages");
    private static FileMessageRepository instance = null;

    public static synchronized FileMessageRepository getInstance() {
        if (instance == null) {
            instance = new FileMessageRepository();
        }
        return instance;
    }

    private FileMessageRepository() {
        try {
            Files.createDirectories(directoryPath); // 디렉토리 생성
        } catch (IOException e) {
            throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID messageId) {
        return directoryPath.resolve("message-" + messageId + ".data");
    }

    private Message loadMessage(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 데이터 읽기 실패: " + filePath, e);
        }
    }

    @Override
    public void save(Message message) {
        Path filePath = getFilePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장 실패: " + message.getId(), e);
        }
    }

    @Override
    public Message findById(UUID id) {
        Path filePath = getFilePath(id);
        if (Files.exists(filePath)) {
            return loadMessage(filePath);
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        try {
            return Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .map(this::loadMessage)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("메세지 목록을 불러오는 중 오류 발생", e);
        }
    }

    @Override
    public void delete(UUID id) {
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
            System.out.println(id + " 메시지 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("메시지 삭제 실패: " + id, e);
        }
    }

    @Override
    public void update(Message message) {
        message.updateTime(System.currentTimeMillis());
        save(message);
        System.out.println(message.getId() + " 메시지 업데이트 완료되었습니다.");
    }
    @Override
    public boolean existsById(UUID id) {
        return Files.exists(getFilePath(id));
    }
}
