package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Primary
@Repository
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.ser";

    @Override
    public void save(Channel channel) {
        if (channel.getId() == null) {
            throw new IllegalArgumentException("채널ID가 공백입니다.");
        }
        List<Channel> channels = readFromFile();
        channels.removeIf(c -> c.getId().equals(channel.getId()));
        channels.add(channel);
        writeToFile(channels);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        if (id == null) return Optional.empty();
        return readFromFile().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(UUID id) {
        if (id == null) return;
        List<Channel> channels = readFromFile();
        boolean removed = channels.removeIf(c -> c.getId().equals(id));
        if (removed) {
            writeToFile(channels);
        }
    }

    @Override
    public List<Channel> findAll() {
        return readFromFile();
    }

    private void writeToFile(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Channel> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<Channel> channels = (List<Channel>) ois.readObject();

            channels.removeIf(channel -> channel.getId() == null);

            return channels;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

}
