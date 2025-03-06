package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public FileChannelService(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        Path directory = Paths.get(System.getProperty("user.dir"),
                "src/main/java/com/sprint/mission/discodeit/data/Channel");
        init(directory);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getUpdatedChannels() {
        return channelRepository.findUpdatedChannels();
    }

    @Override
    public void registerChannel(String channelName, String userName) {
        channelRepository.createChannel(channelName, userRepository.findByName(userName));
    }

    @Override
    public void updateChannel(UUID channelId, String channelName) {
        channelRepository.updateChannel(channelId, channelName);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteChannel(channelId);
        // 해당 채널ID가지는 메시지들 삭제 메서드 추가하기
    }
}
