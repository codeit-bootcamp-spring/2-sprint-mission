package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private Map<UUID, Channel> channelData;
    private static final String CHANNEL_FILE_PATH = "channels.ser";

    public FileChannelRepository() {
        dataLoad();
    }

    private void dataLoad() {
        File file = new File(CHANNEL_FILE_PATH);
        if (!file.exists()) {
            channelData = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(CHANNEL_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            channelData = (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    private void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHANNEL_FILE_PATH))) {
            oos.writeObject(channelData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 저장할 수 없습니다.");
        }
    }

    @Override
    public Channel save(Channel channel){
        this.channelData.put(channel.getId(), channel);

        dataSave();
        return channel;
    }

    @Override
    public Channel update(Channel channel, String newName, String newDescription){
        channel.update(newName, newDescription);

        dataSave();
        return channel;
    }

    @Override
    public Map<UUID, Channel> getChannelData(){
        return channelData;
    }

    @Override
    public List<Channel> findAll(){
        return this.channelData.values().stream().toList();
    }

    @Override
    public Channel findById(UUID channelId){
        return Optional.ofNullable(channelData.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public void delete(UUID channelId){
        channelData.remove(channelId);
        dataSave();
    }
}
