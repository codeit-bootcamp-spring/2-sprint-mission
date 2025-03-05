package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {
    private static volatile FileMessageRepository instance;
    private final Path directory;

    private FileMessageRepository(Path directory) {
        this.directory = directory;
        init(directory);
    }

    public static FileMessageRepository getInstance(Path directory) {
        if (instance == null) {
            synchronized (FileMessageRepository.class) {
                if (instance == null) {
                    instance = new FileMessageRepository(directory);
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
    public void save(Message message) {
        Path filePath = getFilePath(message.getId());
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Path filePath = getFilePath(messageId);
        if (Files.exists(filePath)) {
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return Optional.of((Message) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        return load(directory);
    }

    @Override
    public void delete(UUID id) {
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UUID id, String content) {
        findById(id).ifPresent(message -> {
            message.update(content, System.currentTimeMillis());
            save(message);
        });
    }

    private static <T> List<T> load(Path directory) {
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
