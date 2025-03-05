package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.ser";

    @Override
    public void save(Channel channel) {
        List<Channel> channels = findAll();
        channels.removeIf(ch -> ch.getId().equals(channel.getId())); // 중복 제거
        channels.add(channel);
        writeToFile(channels);
    }

    @Override
    public Channel findById(UUID id) {
        return findAll().stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> findAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeToFile(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID id) {
        List<Channel> channels = findAll();
        boolean removed = channels.removeIf(channel -> channel.getId().equals(id));

        if (removed) {
            writeToFile(channels);
        }
    }
}
