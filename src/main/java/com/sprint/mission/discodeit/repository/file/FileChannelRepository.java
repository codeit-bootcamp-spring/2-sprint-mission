package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final String CHANNEL_FILE;
    private final Map<UUID,Channel> channelData;
    private final SaveLoadHandler<Channel> saveLoadHandler;

    public FileChannelRepository(@Value("${discodeit.repository.file.channel}") String fileName,SaveLoadHandler<Channel> saveLoadHandler) {
        CHANNEL_FILE = fileName;
        this.saveLoadHandler = saveLoadHandler;
        channelData = saveLoadHandler.loadData(CHANNEL_FILE);
    }


    @Override
    public Channel save(Channel channel) {
        channelData.put(channel.getId(), channel);
        saveLoadHandler.saveData(CHANNEL_FILE ,channelData);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return channelData.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }


    @Override
    public void delete(UUID id) {
        if(!channelData.containsKey(id)){
            throw new NoSuchElementException("채널 " + id + "가 존재하지 않습니다.");
        }
        channelData.remove(id);
        saveLoadHandler.saveData(CHANNEL_FILE,channelData);
    }
}
