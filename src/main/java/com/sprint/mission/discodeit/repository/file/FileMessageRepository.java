package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileMessageRepository implements MessageRepository {

    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getMessage());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public Message save(Message message) {
        return FileUtil.saveToFile(DIRECTORY, message, message.getId());
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return FileUtil.loadFromFile(DIRECTORY, id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
                .filter(object -> object instanceof Message)
                .map(object -> (Message) object)
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        FileUtil.deleteFile(DIRECTORY, id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        findAllByChannelId(channelId)
                .forEach(message ->this.deleteById(message.getId()));
    }

    @Override
    public Optional<Instant> findLatestCreatedAtByChannelId(UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
                .map(obj -> (Message) obj)
                .filter(message -> message.getChannelId().equals(channelId)) // 해당 채널의 메시지만 필터링
                .max(Comparator.comparing(Message::getCreatedAt)) // 가장 최신 메시지 찾기
                .map(Message::getCreatedAt); // createdAt 값만 반환
    }

}
