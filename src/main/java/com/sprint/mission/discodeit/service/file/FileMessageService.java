package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static volatile FileMessageService instance;
    private final Path directory;
    private final UserService userService;
    private final ChannelService channelService;

    private FileMessageService(Path directory, UserService userService, ChannelService channelService) {
        this.directory = directory;
        this.userService = userService;
        this.channelService = channelService;
        init(directory);
    }

    public static FileMessageService getInstance(Path directory, UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (FileMessageService.class) {
                if (instance == null) {
                    instance = new FileMessageService(directory, userService, channelService);
                }
            }
        }
        return instance;
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void create(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message 객체가 null 입니다.");
        }

        userService.findById(message.getUserId());
        channelService.findById(message.getChannelId());

        save(getFilePath(message.getId()), message);
    }

    @Override
    public Message findById(UUID messageId) {
        Path filePath = getFilePath(messageId);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                return (Message) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Failed to read message data", e);
            }
        }else {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
    }

    @Override
    public List<Message> findAll() {
        return load(directory);
    }

    @Override
    public void delete(UUID messageId) {
        Path filePath = getFilePath(messageId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = findById(messageId);
        message.update(content, System.currentTimeMillis());
        save(getFilePath(messageId), message);
    }

    private <T> void save(Path filePath, T data) {
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> load(Path directory) {
        if (Files.exists(directory)) {
            try {
                return Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                return (T) ois.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    private Path getFilePath(UUID id) {
        return directory.resolve(id.toString().concat(".ser"));
    }
}
