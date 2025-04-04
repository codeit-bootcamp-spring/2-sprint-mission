package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
public class FileChannelRepository implements ChannelRepository {
    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "channel");

    public FileChannelRepository() {
        init();
    }


    @Override
    public Channel save(Channel channel) {
        Path path = DIRECTORY.resolve(channel.getId() + ".ser");
        saveToFile(path, channel);
        return channel;
    }

    private void init() {
        if (!Files.exists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveToFile(Path path, Channel channel) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Optional<Channel> loadToId(UUID id) {
        Path path = DIRECTORY.resolve(id + ".ser");
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            if (obj instanceof Channel) {
                return Optional.of((Channel) obj);
            }
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Channel> load() {
        try (Stream<Path> path = Files.list(DIRECTORY)) {
            return path
                    .map(this::loadFromFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Channel loadFromFile(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void remove(Channel channel) {
        try {
            if (channel != null && Files.exists(DIRECTORY.resolve(channel.getId() + ".ser"))) {
                Files.delete(DIRECTORY.resolve(DIRECTORY.resolve(channel.getId() + ".ser")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
