package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileChannelService implements ChannelService {

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        saveToFile(channel);

        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = loadOneFromFile(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return loadAllFromFile().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channelNullable = loadOneFromFile(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (loadOneFromFile(channelId) == null) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        deleteFile(channelId);
    }


    // ============================== 직렬화 관련 로직 ===================================
    @Override
    public void saveToFile(Channel channel) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");
        Path filePath = directory.resolve(channel.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, channel);

    }

    @Override
    public Channel loadOneFromFile(UUID channelId) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");
        return SerializationUtil.reverseOneSerialization(directory,channelId);
    }

    @Override
    public List<Channel> loadAllFromFile() {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFile(UUID channelId) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");
        Path filePath = directory.resolve(channelId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("채널 파일 삭제 예외 발생 : " + e.getMessage());
        }

    }
}
