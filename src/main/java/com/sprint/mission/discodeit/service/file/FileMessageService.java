package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

public class FileMessageService implements MessageService {
    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data", "messages");
    private static volatile FileMessageService instance = null;
    private final UserService userService;
    private final ChannelService channelService;

    private FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Message 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    public static FileMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (FileMessageService.class) {
                if (instance == null) {
                    instance = new FileMessageService(userService, channelService);
                }
            }
        }

        return instance;
    }

    private void save(Message message) {
        Path filePath = getFilePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(message);
        } catch (IOException e) {
            System.out.println(message.getId() + " Message 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID messageId) {
        return DIRECTORY_PATH.resolve(messageId + ".ser");
    }

    @Override
    public Message create(UUID authorId, UUID channelId, String content) {
        try {
            userService.findById(authorId);
            channelService.findById(channelId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        }

        Message message = new Message(authorId, channelId, content);
        save(message);

        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return findAll().stream()
                .filter(u -> u.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(messageId + " 메세지를 찾을 수 없습니다."));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(u -> u.getId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findByAuthorId(UUID authorId) {
        return findAll().stream()
                .filter(u -> u.getId().equals(authorId))
                .toList();
    }

    @Override
    public List<Message> findAll() {
        try (Stream<Path> files = Files.list(DIRECTORY_PATH)) {
            return files.map(file -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                    return (Message) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(file.getFileName() + " Message 로드를 실패했습니다: " + e.getMessage());
                }
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException("Message 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = findById(messageId);
        message.update(newContent);
        save(message);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Path filePath = getFilePath(messageId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " Message 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
