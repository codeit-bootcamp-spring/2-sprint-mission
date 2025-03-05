package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels;

    public FileChannelRepository() {
        channels = loadFromFile("Channels.csv");
    }

    @Override
    public void create(Channel channel) {
        channels.put(channel.getId(), channel);
        saveInFile(channels, "Channels.csv");
    }

    @Override
    public void update(Channel channel) {
        channels.put(channel.getId(), channel);
        saveInFile(channels, "Channels.csv");
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
        saveInFile(channels, "Channels.csv");
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Channel find(UUID id) {
        return channels.getOrDefault(id, null);
    }

    public static void saveInFile(Map<UUID, Channel> channels, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // 헤더
            bw.write("key,value");
            bw.newLine();

            for (Map.Entry<UUID, Channel> entry : channels.entrySet()) {
                UUID key = entry.getKey();
                Channel value = entry.getValue();
                bw.write(key + "," + value);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<UUID, Channel> loadFromFile(String filename){
        Map<UUID, Channel> loadedMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false; // 첫 줄(헤더) 건너뛰기
                    continue;
                }
                String[] tokens = line.split(",");
                UUID key = UUID.fromString(tokens[0]);
                Channel value = new Channel(tokens[1]);
                loadedMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedMap;
    }
}
