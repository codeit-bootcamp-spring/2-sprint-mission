package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FileMessageRepository implements MessageRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");

    private final List<Message> messagesData;


    public FileMessageRepository() {
        messagesData = new ArrayList<>();

    }

    @Override
    public void save(Message message) {
        init();
        messagesData.add(message);
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


    @Override
    public List<Message> load() {
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


    @Override
    public void deleteFromFile(Message message) {
        try {
            if (message != null && Files.exists(directory.resolve(message.getId() + ".ser"))) {
                Files.delete(directory.resolve(directory.resolve(message.getId() + ".ser")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
