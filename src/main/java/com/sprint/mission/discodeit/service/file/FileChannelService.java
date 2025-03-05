package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static volatile FileChannelService instance;

    private final Path directory;

    public FileChannelService(Path directory) {
        this.directory = directory;
        init(directory);
    }

    public static FileChannelService getInstance(Path directory) {
        if(instance == null) {
            synchronized (FileChannelService.class) {
                if(instance == null) {
                    instance = new FileChannelService(directory);
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
    public void create(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel 객체가 null 입니다.");
        }
        save(getFilePath(channel.getId()), channel);
    }

    @Override
    public Channel findById(UUID channelId) {
        Path filePath = getFilePath(channelId);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                return (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Failed to read channel data", e);
            }
        }else {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
    }

    @Override
    public List<Channel> findAll() {
        return load(directory);
    }

    @Override
    public void delete(UUID channelId) {
        Path filePath = getFilePath(channelId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UUID channelId, String name, String description) {
        Channel channel = findById(channelId);
        channel.update(name, description, System.currentTimeMillis());
        save(getFilePath(channelId), channel);
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
                List<T> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                Object data = ois.readObject();
                                return (T) data;
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    private Path getFilePath(UUID channelId) {
        return directory.resolve(channelId.toString().concat(".ser"));
    }
}
