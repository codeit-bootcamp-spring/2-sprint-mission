package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("fileChannelRepository")
public class FileChannelRepositoryImplement implements ChannelRepository {
    private String DATA_DIR = "data";
    private String CHANNEL_DATA_FILE = "channels.dat";
    
    private final Map<UUID, Channel> channelRepository;
    
    public FileChannelRepositoryImplement() {
        channelRepository = loadData();
    }
    
    public FileChannelRepositoryImplement(String dataDir) {
        DATA_DIR = dataDir;
        CHANNEL_DATA_FILE = "channels.dat";
        channelRepository = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, CHANNEL_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, Channel>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
               throw new RuntimeException(e);
            }
        }
        
        return new ConcurrentHashMap<>();
    }
    
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, CHANNEL_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(channelRepository);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
    
    @Override
    public boolean register(Channel channel) {
        channelRepository.put(channel.getChannelId(), channel);
        saveData();
        return true;
    }

    @Override
    public Set<UUID> allChannelIdList() {
        return new HashSet<>(channelRepository.keySet());
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelRepository.get(channelId));
    }

    @Override
    public Optional<String> findChannelNameById(UUID channelId) {
        return findById(channelId).map(Channel::getChannelName);
    }

    @Override
    public Optional<Channel> findByName(String channelName) {
        return channelRepository.values().stream()
            .filter(channel -> channel.getChannelName() != null && channel.getChannelName().equals(channelName))
            .findFirst();
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        boolean removed = channelRepository.remove(channelId) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }

    @Override
    public boolean updateChannel(Channel channel) {
        if (channel == null || !channelRepository.containsKey(channel.getChannelId())) {
            return false;
        }
        channelRepository.put(channel.getChannelId(), channel);
        saveData();
        return true;
    }
} 