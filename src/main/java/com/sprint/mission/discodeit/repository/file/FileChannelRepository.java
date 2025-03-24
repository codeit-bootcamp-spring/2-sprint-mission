package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Channel updateChannelChannelName(UUID channelUUID, String channelName) {
        Channel channel = findChannelById(channelUUID)
                .orElseThrow(() -> new IllegalArgumentException("채널 찾을 수 없습니다.: " + channelUUID));
        channel.updateChannelName(channelName);
        fileManager.writeToFile(SubDirectory.USER, channel, channel.getId());
        return channel;
    }

    @Override
    public boolean deleteChannelById(UUID channelUUID) {
        return fileManager.deleteFileById(SubDirectory.CHANNEL, channelUUID);
    }
}
