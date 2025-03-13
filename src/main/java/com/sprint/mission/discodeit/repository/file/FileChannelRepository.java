package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileStorageManager;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "src/main/resources/channels.dat";
    private static Map<UUID, Channel> channels = new HashMap<>();
    private final FileStorageManager fileStorageManager;

    public FileChannelRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        channels = fileStorageManager.loadFile(FILE_PATH);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(FILE_PATH, channels);
    }

    @Override
    public void addChannel(Channel channel) {
        channels.put(channel.getId(), channel);
        fileStorageManager.saveFile(FILE_PATH, channels);
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void deleteChannelById(UUID channelId) {
        channels.remove(channelId);
        fileStorageManager.saveFile(FILE_PATH, channels);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channels.containsKey(channelId);
    }
}
