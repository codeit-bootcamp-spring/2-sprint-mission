package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileChannelRepository implements ChannelRepository {

    private final FileManager fileManager;

    @Override
    public Channel save(Channel channel) {
        fileManager.writeToFile(SubDirectory.CHANNEL, channel, channel.getId());
        return channel;
    }

    @Override
    public Optional<Channel> findChannelById(UUID channelUUID) {
        Optional<Channel> channel = fileManager.readFromFileById(SubDirectory.CHANNEL, channelUUID, Channel.class);
        return channel;
    }

    @Override
    public List<Channel> findAllChannel() {
        List<Channel> channelList = fileManager.readFromFileAll(SubDirectory.CHANNEL, Channel.class);
        return channelList;
    }

    @Override
    public void delete(UUID channelUUID) {
        fileManager.deleteFileById(SubDirectory.CHANNEL, channelUUID);
    }
}
