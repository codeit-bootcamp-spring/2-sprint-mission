package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class FileMessageRepository implements MessageRepository {
    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "messages");

    public FileMessageRepository() {
        init();
    }

    @Override
    public Message save(Message message) {
        Path path = DIRECTORY.resolve(message.getId() + ".ser");
        saveToFile(path, message);
        return message;
    }

    private void init() {
        try {
            if (!Files.exists(DIRECTORY)) {
                Files.createDirectories(DIRECTORY);
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


    @Override
    public Optional<Message> loadToId(UUID id) {
        Path path = DIRECTORY.resolve(id + ".ser");
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            if (obj instanceof Message) {
                return Optional.of((Message) obj);
            }
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> load() {
        try (Stream<Path> path = Files.list(DIRECTORY)) {
            return path
                    .map(this::loadFromFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
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


    @Override
    public void remove(Message message) {
        try {
            if (message != null && Files.exists(DIRECTORY.resolve(message.getId() + ".ser"))) {
                Files.delete(DIRECTORY.resolve(DIRECTORY.resolve(message.getId() + ".ser")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
