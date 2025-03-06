package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private volatile static FileChannelService instance = null;
    private static final String FILE_PATH = "channel.dat";
    private final Map<UUID, Channel> channelRepository;

    public FileChannelService() {
        this.channelRepository = loadAll();
    }

    public static FileChannelService getInstance(){
        if(instance == null){
            synchronized (FileChannelService.class){
                if(instance == null){
                    instance = new FileChannelService();
                }
            }
        }
        return instance;
    }

    private Map<UUID, Channel> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(channelRepository);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Channel saveChannel(String name) {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("name is null or empty");
        }
        Channel channel = new Channel(name);
        channelRepository.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public List<Channel> findByName(String name) {
        List<Channel> channels = channelRepository.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .toList();
        if(channels.isEmpty()){
            throw new NoSuchElementException("해당 이름의 채널이 없습니다.");
        }
        return channels;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelRepository.get(id));
    }

    @Override
    public List<Channel> findAll() {
        if(channelRepository.isEmpty()){
            throw new NoSuchElementException("채널이 없습니다.");
        }
        return channelRepository.values().stream().toList();
    }

    @Override
    public void update(UUID id, String name) {
        if(!channelRepository.containsKey(id)){
            throw new NoSuchElementException("채널이 없습니다.");
        }
        if(name == null){
            throw new IllegalArgumentException("수정할 이름은 null일 수 없습니다.");
        }
        channelRepository.get(id).setName(name);
        saveToFile();
    }

    @Override
    public void delete(UUID id) {
        channelRepository.remove(id);
        saveToFile();
    }
}
