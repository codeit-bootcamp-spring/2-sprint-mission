package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository, FileRepository<Channel> {
    private static volatile FileChannelRepository instance;
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");

    // 조회할 떄 마다 파일 I/O를 이용해 로드하기 vs Map을 이용해 메모리에 저장해놓고 꺼내쓰기
    private final Map<UUID, Channel> channelMap;

    public static FileChannelRepository getInstance() {
        if (instance == null) {
            synchronized (FileChannelRepository.class) {
                if (instance == null) {
                    instance = new FileChannelRepository();
                }
            }
        }
        return instance;
    }

    private FileChannelRepository() {
        SerializationUtil.init(directory);
        channelMap = new ConcurrentHashMap<>(); // 멀티쓰레드 환경 고려
        loadCacheFromFile(); // 서버 메모리와 파일 동기화
    }


    @Override
    public Channel save(Channel channel) {
        channelMap.put(channel.getId(), channel); // 메모리에 업데이트
        saveToFile(channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelMap.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        channelMap.remove(channelId);
        deleteFileById(channelId);
    }

    @Override
    public void saveToFile(Channel channel) {
        Path filePath = directory.resolve(channel.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, channel);
    }


    @Override
    public List<Channel> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("채널 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    // 서버 시작 시 기존 저장된 데이터 메모리에 캐싱
    private void loadCacheFromFile() {
        List<Channel> channels = loadAllFromFile();
        for (Channel channel : channels) {
            channelMap.put(channel.getId(), channel);
        }
    }
}
