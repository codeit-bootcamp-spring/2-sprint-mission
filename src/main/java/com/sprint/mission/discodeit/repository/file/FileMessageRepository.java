package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, Message.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + DIRECTORY, e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }

    @Override
    public Message save(Message message) {
        Path path = resolvePath(message.getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장 실패: " + message.getId(), e);
        }
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Path path = resolvePath(messageId);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                Message message = (Message) ois.readObject();
                return Optional.of(message);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("메시지 읽기 실패: " + messageId, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("메시지 읽기 실패: " + path, e);
                        }
                    })
                    .filter(message -> message.getChannelId().equals(channelId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("메시지 목록 로딩 실패", e);
        }
    }

    @Override
    public boolean existsById(UUID messageId) {
        return Files.exists(resolvePath(messageId));
    }

    @Override
    public void deleteById(UUID messageId) {
        Path path = resolvePath(messageId);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("메시지 삭제 실패: " + messageId, e);
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            paths.filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile());
                             ObjectInputStream ois = new ObjectInputStream(fis)) {
                            Message message = (Message) ois.readObject();
                            if (message.getChannelId().equals(channelId)) {
                                Files.deleteIfExists(path);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("메시지 삭제 실패: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("채널에 해당하는 메시지 삭제 실패", e);
        }
    }
}
