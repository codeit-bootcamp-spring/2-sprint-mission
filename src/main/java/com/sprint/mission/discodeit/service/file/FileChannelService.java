package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final String CHANNEL_FILE = "channels.ser";
    private final Map<UUID, Channel> channelData;

    public FileChannelService() {
        this.channelData = loadData();
    }

    private Map<UUID, Channel> loadData(){
        File file = new File(CHANNEL_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)){
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(){
        try(FileOutputStream fos = new FileOutputStream(CHANNEL_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this.channelData);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel create(ChannelType type, String name) {
        Channel channel = new Channel(type, name);
        this.channelData.put(channel.getId(), channel);
        saveData();

        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = channelData.get(channelId);

        return Optional.ofNullable(channelNullable).orElseThrow(() -> new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String newName, ChannelType newType) {       //채널명 수정
        Channel channelNullable = channelData.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable).orElseThrow(() -> new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다."));
        channel.updateChannel(newName);
        channel.updateChannelType(newType);
        saveData();

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if(!channelData.containsKey(channelId)){
            throw new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다.");
        }
        channelData.remove(channelId);
        saveData();
    }

}
