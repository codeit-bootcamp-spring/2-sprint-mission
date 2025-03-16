package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    private Map<UUID, NavigableSet<Message>> channelIdMessages;       // channelId를 Key로 가지는 TreeSet<Message> / List가 아니라 NavigatbaleSet을 사용하는 이유는 구간조회가 빠르기 때문

    public FileMessageRepository() {
        super(Message.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\messagedata"));
        channelIdMessages = new HashMap<>();
    }

    @Override
    public void add(Message newMessage) {
        super.add(newMessage);
        super.saveToFile(super.directory.resolve(newMessage.getId().toString() + ".ser"), newMessage);
        channelIdMessages.computeIfAbsent(newMessage.getChannelId(),
                        id -> new TreeSet<>(
                                Comparator.comparing(Message::getCreatedAt)     // 생성된 시간 순서대로 정렬 (오름차순, 오래된 메세지가 첫번째)
                                        .thenComparing(Message::getId)          // 생성된 시간이 동일한 경우 id 순으로 정렬 (예외 처리를 위해, 정렬 순서에 별 의미는 없음)
                        )
                ).add(newMessage);
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
