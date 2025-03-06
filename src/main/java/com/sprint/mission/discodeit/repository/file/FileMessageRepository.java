package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository {
    private static volatile FileMessageRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장

    private FileMessageRepository() {
        super(Message.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\messagedata"));
    }

    public static FileMessageRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (FileMessageRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new FileMessageRepository();
                }
            }
        }
        return instance;
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
