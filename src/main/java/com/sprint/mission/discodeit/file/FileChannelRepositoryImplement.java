package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelRepository;

import java.io.*;
import java.util.*;

/**
 * Channel 저장소의 파일 기반 구현 클래스
 */
public class FileChannelRepositoryImplement implements ChannelRepository {
    private static final String DATA_DIR = "data";
    private static final String CHANNEL_DATA_FILE = "channels.dat";
    
    // 채널 데이터를 저장할 Map (채널 ID를 키로 사용)
    private Map<UUID, Channel> channelRepository;
    
    public FileChannelRepositoryImplement() {
        loadData();
    }
    
    /**
     * 메모리에 데이터를 파일로부터 로드합니다.
     */
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
                System.err.println("채널 데이터 로드 중 오류 발생: " + e.getMessage());
                channelRepository = new HashMap<>();
            }
        } else {
            channelRepository = new HashMap<>();
            System.out.println("새로운 채널 저장소 생성");
        }
    }
    
    /**
     * 메모리의 데이터를 파일에 저장합니다.
     */
    private void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("데이터 디렉토리 생성 실패: " + DATA_DIR);
                return;
            }
        }
        
        File file = new File(dir, CHANNEL_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(channelRepository);
            System.out.println("채널 데이터 저장 완료: " + channelRepository.size() + "개의 채널");
        } catch (IOException e) {
            System.err.println("채널 데이터 저장 중 오류 발생: " + e.getMessage());
        }
    }
    
    @Override
    public Channel registerChannel(Channel channel) {
        channelRepository.put(channel.getChannelId(), channel);
        saveData(); // 데이터 변경 시 저장
        return channel;
    }
    
    @Override
    public Set<UUID> AllChannelUserList() {
        // 방어적 복사를 통해 원본 데이터 보호
        return new HashSet<>(channelRepository.keySet());
    }

    @Override
    public Optional<Channel> findByChannelId(UUID channelId) {
        return Optional.ofNullable(channelRepository.get(channelId));
    }

    @Override
    public Optional<Channel> findByChannelName(String channelName) {
        return channelRepository.values().stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .findFirst();
    }
    
    @Override
    public boolean removeChannel(UUID channelId) {
        boolean result = channelRepository.remove(channelId) != null;
        if (result) {
            saveData(); // 데이터 변경 시 저장
        }
        return result;
    }
} 