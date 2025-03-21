package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final String fileName = "channel.ser";
    private final Map<UUID, Channel> channelMap;
    private final FileDataManager fileDataManager;

    public FileChannelRepository() {
        this.fileDataManager = new FileDataManager(fileName);
        this.channelMap = loadChannelList();
    }

    public void saveChannelList() {
        fileDataManager.saveObjectToFile(channelMap);
    }

    public Map<UUID, Channel> loadChannelList() {
        Map<UUID, Channel> loadedData = fileDataManager.loadObjectFromFile();
        if (loadedData == null) {
            return new HashMap<>();
        }
        return loadedData;
    }

    @Override
    public Channel save(Channel channel) {
        this.channelMap.put(channel.getId(), channel);
        saveChannelList();
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelMap.get(channelId));
    }

    @Override
    public boolean existsById(UUID id) {
        return channelMap.containsKey(id);  // channelMap에서 id가 존재하는지 확인
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = channelMap.remove(id) != null;
        if (removed) {
            saveChannelList();  // 삭제 후 변경 사항 저장
        }
    }
}
