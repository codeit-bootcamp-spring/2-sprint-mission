package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FileChannelRepository implements ChannelRepository {
    private static final Path directory = Paths.get(System.getProperty("user.dir"), "data", "channel");

    private final List<Channel> channelData;

    public FileChannelRepository() {
        channelData = new ArrayList<>();
    }


    @Override
    public void save(Channel channel) {
        init();
        channelData.add(channel);
        Path path = directory.resolve(channel.getId() + ".ser");
        saveToFile(path, channel);
    }

    private void init() {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
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
    public List<Channel> load() {
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

    private Channel loadFromFile(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteFromFile(Channel channel) {
        try {
            if (channel != null && Files.exists(directory.resolve(channel.getId() + ".ser"))) {
                Files.delete(directory.resolve(directory.resolve(channel.getId() + ".ser")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
