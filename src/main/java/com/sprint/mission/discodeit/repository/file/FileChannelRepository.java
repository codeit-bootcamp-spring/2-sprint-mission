package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository, FileRepository<Channel> {
    private final Path directory;
    private final Map<UUID, Channel> channelMap;

    // Map을 ConcurrentHashMap으로 초기화해주고싶으므로 @RequiredArgsConstructor는 안쓰고 직접 생성자 만듦
    public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String fileDir) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "channels");
        SerializationUtil.init(directory);
        channelMap = new ConcurrentHashMap<>(); // 멀티쓰레드 환경 고려
        loadCacheFromFile(); // 서버 메모리와 파일 동기화
    }


    @Override
    public Channel save(Channel channel) {
        saveToFile(channel);
        channelMap.put(channel.getId(), channel); // 메모리에 업데이트
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelMap.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return channelMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        deleteFileById(id);
        channelMap.remove(id);
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
    public void deleteFileById(UUID id) {
        Path filePath = directory.resolve(id + ".ser");
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
