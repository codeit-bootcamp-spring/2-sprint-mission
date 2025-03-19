package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    private Map<UUID, NavigableSet<Message>> channelIdMessages;       // channelId를 Key로 가지는 TreeSet<Message> / List가 아니라 NavigatbaleSet을 사용하는 이유는 구간조회가 빠르기 때문

    public FileMessageRepository() {
        super(Message.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\messagedata"));
        channelIdMessages = new HashMap<>();
    }

    @Override
    public void addChannelIdToChannelIdMessage(UUID channelId) {
        if (channelIdMessages.containsKey(channelId)) {         // 자체적인 무결성 확보
            throw new IllegalArgumentException("이미 존재하는 채널입니다: " + channelId);
        }
        channelIdMessages.put(channelId, new TreeSet<>(Comparator.comparing(Message::getCreatedAt)
                                                                .thenComparing(Message::getId)));
    }

    @Override
    public void add(Message newMessage) {
        super.add(newMessage);
        super.saveToFile(super.directory.resolve(newMessage.getId().toString() + ".ser"), newMessage);
        channelIdMessages.get(newMessage.getChannelId()).add(newMessage);
    }

    // existsById(),findById(), getAll()  굳이 file을 탐색할 필요 없다고 생각해 storage를 통해 정보 확인, -> 상속 받은걸 사용

    @Override
    public List<Message> findMessageListByChannelId(UUID channelId) {   //해당 channelID를 가진 message가 없을 때, 빈 리스트 반환
        if (channelIdMessages.get(channelId) == null) {         // channelService에서 createChannel을 할때 항상 addChannelIdToChannelIdMessage가 호출되어 확인할 필요 없지만 자체적인 검증로직 필요?
            throw new NullPointerException("해당 channelId를 가진 채널이 아직 생성되지 않았습니다. " + channelId);
        }
        return new ArrayList<>(channelIdMessages.get(channelId));
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateContent(newContent);
            super.saveToFile(super.directory.resolve(messageId.toString() + ".ser"), super.findById(messageId));
        }
    }

    @Override
    public void updateAttachmentIds(UUID messageId, List<UUID> attachmentIds) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateAttachmentIds(attachmentIds);
            super.saveToFile(super.directory.resolve(messageId.toString() + ".ser"), super.findById(messageId));
        }
    }

    @Override
    public void deleteAttachment(UUID messageId, UUID attachmentId) {
        if (existsById(messageId)) {
            super.storage.get(messageId).deleteAttachment(attachmentId);
            super.saveToFile(super.directory.resolve(messageId.toString() + ".ser"), super.findById(messageId));
        }
    }

    @Override
    public void deleteById(UUID messageId) {
        super.deleteById(messageId);
        super.deleteFile(messageId);
        channelIdMessages.get(super.findById(messageId).getChannelId()).remove(super.findById(messageId));
    }
}
