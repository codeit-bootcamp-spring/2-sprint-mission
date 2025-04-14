package com.sprint.discodeit.sprint.repository.file;

import com.sprint.discodeit.sprint.domain.ChannelType;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.repository.util.AbstractFileRepository;
import com.sprint.discodeit.sprint.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;


@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    protected FileChannelRepository() {
        super(FilePathUtil.CHANNELS.getPath());
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Map<UUID, Channel> channelMap = loadAll();
        return Optional.ofNullable(channelMap.get(channelId));
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
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + channel.getId());
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

    public List<Channel> findByChnnelType(ChannelType channelType) {
        Map<UUID, Channel> channelMap = loadAll();
        return channelMap.values().stream()
                .filter(channel -> channel.getType().equals(channelType))
                .collect(Collectors.toList());
    }

    public List<Channel> findByChannelType(ChannelType channelType){
        Map<UUID, Channel> channelMap = loadAll();
        return channelMap.values().stream()
                .filter(channel -> channel.getType().equals(channelType))
                .collect(Collectors.toList());
    }

    public List<Channel> findByIdAll(List<UUID> privateChannelIds) {
        Map<UUID, Channel> channelMap = loadAll();

        return privateChannelIds.stream()
                .map(channelMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
