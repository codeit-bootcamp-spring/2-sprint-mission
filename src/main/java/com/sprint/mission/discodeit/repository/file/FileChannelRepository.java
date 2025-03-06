package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileChannelRepository implements ChannelRepository {
    Path directory = Paths.get(System.getProperty("user.dir"),
            "src/main/java/com/sprint/mission/discodeit/data/Channel");

    @Override
    public boolean channelExists(UUID channelId) {
        return !Files.exists(directory.resolve(channelId + ".ser"));
    }

    private void save(UUID channelId, Channel channel) {
        Path filePath = directory.resolve(channelId + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패" + e.getMessage());
        }
    }

    private List<Channel> loadAllChannelsFile() {
        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("디렉토리가 생성되어 있지 않음.");
        }
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.map(path -> {
                try (
                        FileInputStream fis = new FileInputStream(path.toFile());
                        ObjectInputStream ois = new ObjectInputStream(fis)
                ) {
                    return (Channel) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("파일 로드 실패: " + e.getMessage(), e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("파일 로드 실패" + e.getMessage());
        }
    }

    private void deleteChannelFile(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 불가" + e.getMessage());
            }
        }
    }

    @Override
    public Channel findById(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패: " + e.getMessage());
        }
    }

    @Override
    public List<Channel> findAll() {
        return Optional.ofNullable(loadAllChannelsFile())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findUpdatedChannels() {
        return loadAllChannelsFile().stream()
                .filter(channel -> channel.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createChannel(String channelName, User user) {
        Channel channel = new Channel(channelName, user);
        save(channel.getId(), channel);
    }

    @Override
    public void updateChannel(UUID channelId, String channelName) {
        Channel channel = findById(channelId);
        deleteChannelFile(channel.getId());
        channel.updateChannel(channelName);
        save(channel.getId(), channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        deleteChannelFile(channelId);
    }

    @Override
    public List<UUID> channelListByuserId(UUID userID) {
        return loadAllChannelsFile().stream()
                .filter(channel -> channel.getUser().getId().equals(userID))
                .map(Channel::getId)
                .toList();
    }

    @Override
    public void deleteChannelList(List<UUID> channelIdList) {
        channelIdList.forEach(this::deleteChannelFile);
    }
}

