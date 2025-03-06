package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final Map<UUID, Channel> channels = new HashMap<>();

    public FileChannelService() {
        loadFromFile("Channels.csv");
    }

    @Override
    public void create(Channel channel) {
        channels.put(channel.getId(), channel);
        saveInFile(channels, "Channels.csv");
    }

    @Override
    public Channel find(UUID id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
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
    public void addMember(UUID channelId, User user) {
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channel.addMember(user);
        update(channel);
        System.out.println("유저 [" + user.getName() + "]가 채널 [" + channel.getChannelName() + "]에 추가되었습니다.");
    }

    @Override
    public void removeMember(UUID channelId, User user) {
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channel.removeMember(user);
        update(channel);
        System.out.println("유저 [" + user.getName() + "]가 채널 [" + channel.getChannelName() + "]에서 제거되었습니다.");
    }

    @Override
    public List<User> findMembers(UUID channelId) {
        Channel channel = find(channelId);
        if (channel == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        ArrayList<User> members = new ArrayList<>(channel.getMembers());
        System.out.println("채널 [" + channel.getChannelName() + "]에 등록된 유저 목록: " + members);
        return members;
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
