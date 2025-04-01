package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileChannelRepository implements ChannelRepository {

    private final Path directoryPath;

    public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String directoryPath) {
        this.directoryPath = Paths.get(System.getProperty("user.dir"), directoryPath, "channels");
        init();
    }

    private void init() {
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException("Channel 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public Channel save(Channel channel) {
        Path filePath = getFilePath(channel.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(channel);
            return channel;
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " Channel 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID channelId) {
        return directoryPath.resolve(channelId + ".ser");
    }

    @Override
    public List<Channel> findAll() {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths.map(this::readUserFromFile).toList();
        } catch (IOException e) {
            throw new RuntimeException("Channels 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    private Channel readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(filePath.getFileName() + " Channel 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public Channel findById(UUID channelId) {
        Path filePath = getFilePath(channelId);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("Channel ID " + channelId + "에 해당하는 파일을 찾을 수 없습니다.");
        }

        return readUserFromFile(filePath);
    }

    @Override
    public void delete(UUID channelId) {
        Path filePath = getFilePath(channelId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " Channel 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
