package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.ser";

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> channels = loadChannels();
        channels.put(channel.getId(), channel);
        saveChannels(channels);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        Map<UUID, Channel> channels = loadChannels();
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadChannels().values());
    }

    @Override
    public Channel update(Channel channel) {
        return save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> channels = loadChannels();
        channels.remove(channelId);
        saveChannels(channels);
    }

    @Override
    public boolean exists(UUID channelId) {
        Map<UUID, Channel> channels = loadChannels();
        return channels.containsKey(channelId);
    }

    @Override
    public boolean existsById(UUID channelId) {
        Map<UUID, Channel> channels = loadChannels();
        return channels.containsKey(channelId);
    }

    private Map<UUID, Channel> loadChannels() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File not found. Generating new one.");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("err. Returning new one.");
            return new HashMap<>();
        }
    }

    private void saveChannels(Map<UUID, Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
            System.out.println("channels saved.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
