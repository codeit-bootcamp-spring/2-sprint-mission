package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("fileChannelRepositoryImplement")
public class FileChannelRepositoryImplement implements ChannelRepository {
    private String dataDir;
    private String channelDataFile;

    private final Map<UUID, Channel> channelRepository;

    public FileChannelRepositoryImplement() {
        this.dataDir = "./data";
        this.channelDataFile = "channels.dat";
        channelRepository = loadData();
    }

    public FileChannelRepositoryImplement(String dataDir) {
        this.dataDir = dataDir;
        this.channelDataFile = "channels.dat";
        channelRepository = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadData() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, channelDataFile);
        System.out.println("채널 데이터 로드 경로: " + file.getAbsolutePath());

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, Channel>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("채널 데이터 로드 오류: " + e.getMessage());
                return new ConcurrentHashMap<>();
            }
        }

        return new ConcurrentHashMap<>();
    }

    private synchronized void saveData() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, channelDataFile);
        System.out.println("채널 데이터 저장 경로: " + file.getAbsolutePath());

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(channelRepository);
        } catch (IOException e) {
            System.err.println("채널 데이터 저장 오류: " + e.getMessage());
            throw new RuntimeException("채널 데이터 저장 실패", e);
        }
    }

    @Override
    public boolean register(Channel channel) {
        channelRepository.put(channel.getChannelId(), channel);
        saveData();
        return true;
    }

    @Override
    public Set<UUID> allChannelIdList() {
        return new HashSet<>(channelRepository.keySet());
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelRepository.get(channelId));
    }

    @Override
    public Optional<String> findChannelNameById(UUID channelId) {
        return findById(channelId).map(Channel::getChannelName);
    }

    @Override
    public Optional<Channel> findByName(String channelName) {
        return channelRepository.values().stream()
                .filter(channel -> channel.getChannelName() != null && channel.getChannelName().equals(channelName))
                .findFirst();
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        boolean removed = channelRepository.remove(channelId) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }

    @Override
    public boolean updateChannel(Channel channel) {
        if (channel == null || !channelRepository.containsKey(channel.getChannelId())) {
            return false;
        }
        channelRepository.put(channel.getChannelId(), channel);
        saveData();
        return true;
    }

    // 애플리케이션 종료 시 데이터 저장 보장
    @PreDestroy
    public void saveDataOnShutdown() {
        System.out.println("애플리케이션 종료 - 채널 데이터 저장 중");
        saveData();
    }
}