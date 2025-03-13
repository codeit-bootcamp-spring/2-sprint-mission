package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    public FileMessageRepository() {
        super(Message.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\messagedata"));
    }

    @Override
    public void add(Message newMessage) {
        super.add(newMessage);
        super.saveToFile(super.directory.resolve(newMessage.getId().toString() + ".ser"), newMessage);
    }

    // existsById(),findById(), getAll()  굳이 file을 탐색할 필요 없다고 생각해 storage를 통해 정보 확인, -> 상속 받은걸 사용

    @Override
    public void deleteById(UUID messageId) {
        super.deleteById(messageId);
        super.deleteFile(messageId);
    }

    public List<Message> findMessageListByChannelId(UUID channelId) {   //해당 channelID를 가진 message가 없을 때, 빈 리스트 반환
        if (channelId == null) {
            throw new IllegalArgumentException("input channelId is null!!!");
        }
        return super.storage.values().stream()
                .filter((m) -> Objects.equals(channelId, m.getChannelId()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateContent(newContent);
        }
    }
}
