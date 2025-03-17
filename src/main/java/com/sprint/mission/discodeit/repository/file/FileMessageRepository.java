package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.*;

@Repository
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
    public void deleteById(UUID messageId) {
        super.deleteById(messageId);
        super.deleteFile(messageId);
        channelIdMessages.get(super.findById(messageId).getChannelId()).remove(super.findById(messageId));
    }

    @Override
    public List<Message> findMessageListByChannelId(UUID channelId) {   //해당 channelID를 가진 message가 없을 때, 빈 리스트 반환
        if (channelId == null) {
            throw new IllegalArgumentException("input channelId is null!!!");
        }
        NavigableSet<Message> messages = channelIdMessages.get(channelId);
        if (messages == null) {
            throw new NullPointerException("해당 ");
        }
        return new ArrayList<>(channelIdMessages.get(channelId));
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        if (existsById(messageId)) {
            super.storage.get(messageId).updateContent(newContent);
        }
    }
}
