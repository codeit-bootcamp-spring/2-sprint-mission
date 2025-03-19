package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.model.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {
    public FileChannelRepository() {
        super(Channel.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\channeldata"));   // 현재 프로그램이 실행되고 있는 디렉토리로 설정
    }

    @Override
    public void add(Channel newChannel) {
        super.add(newChannel);
        super.saveToFile(super.directory.resolve(newChannel.getId().toString() + ".ser"), newChannel);
    }

    // existsById(),findById(), getAll()  굳이 file을 탐색할 필요 없다고 생각해 storage를 통해 정보 확인, -> 상속 받은걸 사용
    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return super.storage.values().stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC || (channel.getParticipantIds().contains(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        Channel findChannel = super.findById(channelId);
        findChannel.updateChannelName(newChannelName);
        super.saveToFile(super.directory.resolve(channelId.toString() + ".ser"), findChannel);
    }

    public void addParticipant(UUID channelId, UUID newParticipantId) {
        Channel findChannel = super.findById(channelId);
        findChannel.addParticipant(newParticipantId);
        super.saveToFile(super.directory.resolve(channelId.toString() + ".ser"), findChannel);
    }

    @Override
    public void deleteById(UUID channelId) {
        super.deleteById(channelId);
        super.deleteFile(channelId);
    }
}
