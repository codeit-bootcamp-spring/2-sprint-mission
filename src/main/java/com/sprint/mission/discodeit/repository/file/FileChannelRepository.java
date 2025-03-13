package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.UUID;

@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {
    private static volatile FileChannelRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장

    private FileChannelRepository() {
        super(Channel.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\channeldata"));   // 현재 프로그램이 실행되고 있는 디렉토리로 설정
    }

    public static FileChannelRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (FileChannelRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new FileChannelRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(Channel newChannel) {
        super.add(newChannel);
        super.saveToFile(super.directory.resolve(newChannel.getId().toString() + ".ser"), newChannel);
    }

    // existsById(),findById(), getAll()  굳이 file을 탐색할 필요 없다고 생각해 storage를 통해 정보 확인, -> 상속 받은걸 사용

    @Override
    public void deleteById(UUID channelId) {
        super.deleteById(channelId);
        super.deleteFile(channelId);
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        if (existsById(channelId)) {
            super.storage.get(channelId).updateChannelName(newChannelName);
        }
    }

    public void addParticipant(UUID channelId, UUID newParticipantId) {
        super.findById(channelId).addParticipant(newParticipantId);       // 뒤의 addParticpant는 Channel 의 메서드
    }
}
