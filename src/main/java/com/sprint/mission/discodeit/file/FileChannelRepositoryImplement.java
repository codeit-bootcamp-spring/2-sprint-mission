package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.util.ValidationUtil;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.util.ValidationUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FileChannelRepositoryImplement implements ChannelRepository {
    private static final String DATA_DIR = "data";
    private static final String CHANNEL_DATA_FILE = "channels.dat";
    
    // 채널 데이터를 저장할 Map (채널 ID를 키로 사용)
    private Map<UUID, Channel> channelRepository;
    
    // 싱글톤 인스턴스
    private static FileChannelRepositoryImplement instance;
    
    // private 생성자로 변경
    private FileChannelRepositoryImplement() {
        loadData();
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized FileChannelRepositoryImplement getInstance() {
        if (instance == null) {
            instance = new FileChannelRepositoryImplement();
        }
        return instance;
    }
    
     //메모리에 데이터를 파일로부터 로드합니다. 
    @SuppressWarnings("unchecked")
    private void loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, CHANNEL_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                channelRepository = (Map<UUID, Channel>) in.readObject();
                System.out.println("채널 데이터 로드 완료: " + channelRepository.size() + "개의 채널");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("채널 데이터 로드 중 오류 발생: " + e.getMessage());
            }
        } else {
            channelRepository = new ConcurrentHashMap<>();//동시성 문제
            System.out.println("새로운 채널 저장소 생성");
        }
    }
    
    /**
     * 메모리의 데이터를 파일에 저장합니다.
     */
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("데이터 디렉토리 생성 실패: " + DATA_DIR);
            }
        }
        
        File file = new File(dir, CHANNEL_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(channelRepository);
            System.out.println("채널 데이터 저장 완료: " + channelRepository.size() + "개의 채널");
        } catch (IOException e) {
            throw new RuntimeException("채널 데이터 저장 중 오류 발생: " + e.getMessage());
        }
    }
    @Override
    public Channel register(Channel channel) {
        channelRepository.put(channel.getChannelId(), channel);
        saveData();
        return channel;
    }

    @Override
    public Set<UUID> allChannelIdList() {
        return new HashSet<>(channelRepository.keySet());
    }

    @Override
    public Optional<Channel> findChannelById(UUID channelId) {
        return Optional.ofNullable(channelRepository.get(channelId));
    }

    @Override
    public Optional<String> findChannelNameById(UUID channelId) {
        return findChannelById(channelId).map(Channel::getChannelName);
    }

    @Override
    public Optional<Channel> findChannelByName(String channelName) {
        return channelRepository.values().stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst();
    }

    @Override
    public boolean deleteChannel(UUID channelId) {
        boolean removed = channelRepository.remove(channelId) != null;
        saveData();
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

} 