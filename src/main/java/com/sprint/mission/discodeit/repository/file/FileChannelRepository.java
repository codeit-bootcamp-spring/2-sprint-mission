package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.ser";

    @Override
    public void save(Channel channel) {
        if (channel.getId() == null) {
            throw new IllegalArgumentException("Channel ID cannot be null!");
        }

        List<Channel> channels = readFromFile();
        channels.removeIf(c -> c.getId().equals(channel.getId()));
        channels.add(channel);
        writeToFile(channels);
    }


    @Override
    public Channel findById(UUID id) {
        System.out.println("Searching for Channel with ID: " + id);
        List<Channel> channels = readFromFile();
        Channel found = channels.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (found == null) {
            System.err.println("Channel with ID " + id + " not found.");
        }
        return found;
    }




    @Override
    public void delete(UUID id) {
        List<Channel> channels = readFromFile();
        boolean removed = channels.removeIf(c -> c.getId().equals(id));
        if (removed) {
            writeToFile(channels);
            System.out.println("Channel with ID " + id + " deleted.");
        } else {
            System.err.println("Failed to delete. Channel with ID " + id + " not found.");
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
