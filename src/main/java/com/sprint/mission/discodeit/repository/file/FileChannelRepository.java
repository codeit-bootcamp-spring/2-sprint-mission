package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileChannelRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Channel.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path getFilePath(UUID channelId) {
        return DIRECTORY.resolve("channel-" + channelId + EXTENSION);
    }

    public void serialize(Channel channel) {
        Path path = getFilePath(channel.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 데이터를 저장하는 중 오류 발생: " + path, e);
        }
    }

    public Channel deserialize(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 파일을 읽는 중 오류 발생: " + path, e);
        }
    }

    @Override
    public Channel save(Channel channel) {
        serialize(channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Path path = getFilePath(channelId);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(deserialize(path));
    }

    @Override
    public List<Channel> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserialize(path))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("채널 목록을 불러오는 중 오류 발생", e);
        }
    }

    @Override
    public boolean existsById(UUID channelId) {
        return Files.exists(getFilePath(channelId));
    }

    @Override
    public void delete(UUID channelId) {
        Path path = getFilePath(channelId);
        try {
            Files.delete(path);
            System.out.println(channelId + " 채널 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("채널 삭제 실패: " + channelId, e);
        }
    }

}
