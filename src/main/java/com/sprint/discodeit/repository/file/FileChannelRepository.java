package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.entity.Channel;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.ChannelRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    protected FileChannelRepository() {
        super(FilePathUtil.CHANNELS.getPath());
    }

    @Override
    public Channel findById(String uuId) {
        Map<UUID, Channel> channelMap = loadAll();
        return Optional.ofNullable(channelMap.get(UUID.fromString(uuId))).orElseThrow(() -> new NullPointerException(uuId.toString() + " 는  없는 채널 입니다"));
    }

    @Override
    public List<Channel> findByAll() {
        Map<UUID, Channel> channelMap = loadAll();
        return channelMap.values().stream().toList();
    }

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> channelMap = loadAll();
        if (channelMap.containsKey(channel.getId())) {
            System.out.println("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + channel.getId());
        }
        channelMap.put(channel.getId(), channel);
        writeToFile(channelMap);
    }

    @Override
    public void delete(UUID uuId) {
        Map<UUID, Channel> channelMap = loadAll();
        channelMap.remove(uuId);
        writeToFile(channelMap);
    }
}
