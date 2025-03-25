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

    public FileMessageRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getMessage());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public UUID createMessage(Message message) {
        return FileUtil.saveToFile(DIRECTORY, message, message.getId());
    }

    @Override
    public Message findById(UUID id) {
        return (Message) FileUtil.loadFromFile(DIRECTORY, id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof Message)
                .map(object -> (Message) object)
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        checkMessageExists(id);
        Message message = findById(id);

        message.update(content, attachmentIds);
        FileUtil.saveToFile(DIRECTORY, message, id);
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        checkMessageExists(id);

        FileUtil.deleteFile(DIRECTORY, id);
    }

    @Override
    public Optional<Instant> findLatestCreatedAtByChannelId(UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .map(obj -> (Message) obj)
                .filter(message -> message.getChannelId().equals(channelId)) // 해당 채널의 메시지만 필터링
                .max(Comparator.comparing(Message::getCreatedAt)) // 가장 최신 메시지 찾기
                .map(Message::getCreatedAt); // createdAt 값만 반환
    }

    @Override
    public void deleteMessageByChannelId(UUID channelId) {
        Message message = FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof Message)
                .map(object -> (Message) object)
                .filter(messageObj -> messageObj.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 채널의 메시지를 찾을 수 없습니다: " + channelId));

        FileUtil.deleteFile(DIRECTORY, message.getId());
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkMessageExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id);
        }
    }

}
