package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.domain.entity.Message;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.MessageRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {

    public FileMessageRepository() {
        super(FilePathUtil.MESSAGE.getPath());
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Map<UUID, Message> messageMap = loadAll();
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public List<Message> findByAll() {
        Map<UUID, Message> messageMap = loadAll();
        return messageMap.values().stream().toList();
    }

    @Override
    public void save(Message message) {
        Map<UUID, Message> messageMap = loadAll();
        if (messageMap.containsKey(message.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + message.getId());
        } else {
            messageMap.put(message.getId(), message);
            writeToFile(messageMap);
        }
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> messageMap = loadAll();
        messageMap.remove(messageId);
        writeToFile(messageMap);
    }

    public List<Message> findByChannelAndMessageContext(UUID channelId){
        Map<UUID, Message> messageMap = loadAll();
        return messageMap.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    public Optional<List<Message>> findByChannelId(UUID channelId) {
        Map<UUID, Message> messageMap = loadAll();
        return Optional.of(messageMap.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList()));
    }

    public Instant findLatestMessageTimeByChannelId(UUID channelId) {
        Map<UUID, Message> messageMap = loadAll();
        return messageMap.values().stream().filter( message -> message.getChannelId().equals(channelId))
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
