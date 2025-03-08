package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "channel");

    public FileChannelRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public void createChannel(Channel channel) {
        FileUtil.saveToFile(DIRECTORY, channel, channel.getId());
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        FileUtil.loadFromFile(DIRECTORY, id).ifPresent(object -> {
            if (object instanceof Channel channel) {
                userMembers.forEach(channel::addMember);
                FileUtil.saveToFile(DIRECTORY, channel, id);
            } else {
                throw new IllegalArgumentException("Channel 타입의 객체가 아닙니다. " + id);
            }
        });
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        FileUtil.loadFromFile(DIRECTORY, id).ifPresent(object -> {
            if (object instanceof Channel channel) {
                userMembers.forEach(channel::removeMember);
                FileUtil.saveToFile(DIRECTORY, channel, id);
            } else {
                throw new IllegalArgumentException("Channel 타입의 객체가 아닙니다. " + id);
            }
        });
    }

    @Override
    public Optional<Channel> selectChannelById(UUID id) {
        return FileUtil.loadFromFile(DIRECTORY, id);
    }

    @Override
    public List<Channel> selectAllChannels() {
        return FileUtil.loadAllFiles(DIRECTORY);
    }

    @Override
    public void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId) {
        FileUtil.loadFromFile(DIRECTORY, id).ifPresent(object -> {
            if (object instanceof Channel channel) {
                channel.updateName(name);
                channel.updateCategory(category);
                channel.updateType(type);
                FileUtil.saveToFile(DIRECTORY, channel, id);
            } else {
                throw new IllegalArgumentException("Channel 타입의 객체가 아닙니다. " + id);
            }
        });
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        FileUtil.deleteFile(DIRECTORY, id);
    }
}
