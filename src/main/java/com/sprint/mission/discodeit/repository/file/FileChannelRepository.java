package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private static final Path directoryPath = Paths.get("data/channels");
    private static FileChannelRepository instance = null;

    public static synchronized FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }
        return instance;
    }

    private FileChannelRepository() {
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + e.getMessage());
        }
    }

    // 파일 경로를 생성하는 메서드
    private Path getFilePath(UUID channelId) {
        return directoryPath.resolve("channel-" + channelId + ".data");
    }

    // 파일에서 Channel 객체를 읽어오는 메서드
    private Channel loadChannel(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 데이터 읽기 실패: " + filePath, e);
        }
    }

    private void saveChannel(Channel channel) {
        Path filePath = getFilePath(channel.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패: " + channel.getId(), e);
        }
    }

    @Override
    public void save(Channel channel) {
        saveChannel(channel);
    }

    @Override
    public Channel findById(UUID id) {
        Path filePath = getFilePath(id);
        if (Files.exists(filePath)) {
            return loadChannel(filePath);
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        try {
            return Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .map(this::loadChannel)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("채널 목록을 불러오는 중 오류 발생", e);
        }
    }

    @Override
    public void delete(UUID id) {
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
            System.out.println(id + " 채널 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("채널 삭제 실패: " + id, e);
        }
    }

    @Override
    public void update(Channel channel) {
        channel.updateTime(System.currentTimeMillis());
        saveChannel(channel);
        System.out.println(channel.getId() + " 채널 업데이트 완료되었습니다.");
    }
    @Override
    public boolean existsById(UUID id) {
        return Files.exists(getFilePath(id));
    }
}
