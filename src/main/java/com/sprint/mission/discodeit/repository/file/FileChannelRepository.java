package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository, FileRepository<Channel> {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");

    @Override
    public Channel save(Channel channel) {
        saveToFile(channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return loadOneFromFileById(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return loadAllFromFile();
    }

    @Override
    public void deleteById(UUID channelId) {
        deleteFileById(channelId);
    }

    @Override
    public void saveToFile(Channel channel) {
        Path filePath = directory.resolve(channel.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, channel);
    }

    @Override
    public Channel loadOneFromFileById(UUID channelId) {
        return SerializationUtil.reverseOneSerialization(directory,channelId);
    }

    @Override
    public List<Channel> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("채널 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }
}
