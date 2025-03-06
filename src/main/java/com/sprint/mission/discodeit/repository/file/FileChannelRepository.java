package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "src/main/resources/channels.dat";
    private static Map<UUID, Channel> channels = new HashMap<>();

    public FileChannelRepository() {
        loadFile();
    }

    private void loadFile(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            channels = (Map<UUID, Channel>) ois.readObject();
        }catch (EOFException e) {
            System.out.println("⚠ channels.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 로드 중 오류 발생", e);
        }
    }

    private void saveFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 중 오류 발생", e);
        }
    }

    @Override
    public void save(Channel channel) {
        channels.put(channel.getId(), channel);
        saveFile();
    }

    @Override
    public Channel findById(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        channels.remove(channelId);
        saveFile();
    }

    @Override
    public boolean exists(UUID channelId) {
        return channels.containsKey(channelId);
    }
}
