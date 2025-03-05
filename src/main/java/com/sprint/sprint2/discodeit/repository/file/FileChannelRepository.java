package com.sprint.sprint2.discodeit.repository.file;

import com.sprint.sprint2.discodeit.entity.Channel;
import com.sprint.sprint2.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    private static final String FILE_PATH = "channel.ser";

    protected FileChannelRepository(String filePath) {
        super(filePath);
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
