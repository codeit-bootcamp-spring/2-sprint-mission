package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
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

public class FileChannelService implements ChannelService {
    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data", "channels");
    private static volatile FileChannelService instance = null;

    private FileChannelService() {
        init();
    }

    private void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Channel 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    public static FileChannelService getInstance() {
        if (instance == null) {
            synchronized (FileChannelService.class) {
                if (instance == null) {
                    instance = new FileChannelService();
                }
            }
        }

        return instance;
    }

    private void save(Channel channel) {
        Path filePath = getFilePath(channel.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            System.out.println(channel.getId() + " Channel 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID channelId) {
        return DIRECTORY_PATH.resolve(channelId + ".ser");
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel newChannel = new Channel(type, name, description);
        save(newChannel);

        return newChannel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return findAll().stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        try (Stream<Path> files = Files.list(DIRECTORY_PATH)) {
            return files.map(file -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                    return (Channel) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(file.getFileName() + " Channel 로드를 실패했습니다: " + e.getMessage());
                }
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException("Channel 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = findById(channelId);
        channel.update(newName, newDescription);
        save(channel);

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Path filePath = getFilePath(channelId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " Channel 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
