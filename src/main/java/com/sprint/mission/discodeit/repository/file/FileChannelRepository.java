package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileChannelRepository implements ChannelRepository {

    private final Path DIRECTORY;

    public FileChannelRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getChannel());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public UUID createChannel(Channel channel) {
        return FileUtil.saveToFile(DIRECTORY, channel, channel.getId());
    }

    @Override
    public void addMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        Channel channel = findById(id);

        userMembers.forEach(channel::addMember);
        FileUtil.saveToFile(DIRECTORY, channel, id);
    }

    @Override
    public void removeMembers(UUID id, Set<UUID> userMembers, UUID userId) {
        Channel channel = findById(id);

        userMembers.forEach(channel::removeMember);
        FileUtil.saveToFile(DIRECTORY, channel, id);
    }

    @Override
    public Channel findById(UUID id) {
        return (Channel) FileUtil.loadFromFile(DIRECTORY, id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Channel> findAll() {
        return FileUtil.loadAllFiles(DIRECTORY);
    }

    @Override
    public List<Channel> findByType(ChannelType type) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .map(obj -> (Channel) obj)
                .filter(channel -> channel.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findByUserIdAndType(UUID userId, ChannelType type) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .map(obj -> (Channel) obj)
                .filter(channel -> channel.getUserId().equals(userId) && channel.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(UUID id, String name, String category, ChannelType type, UUID userId) {
        checkChannelExists(id);
        Channel channel = findById(id);

        channel.update(name, category, type);
        FileUtil.saveToFile(DIRECTORY, channel, id);
    }

    @Override
    public void deleteChannel(UUID id, UUID userId) {
        checkChannelExists(id);

        FileUtil.deleteFile(DIRECTORY, id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkChannelExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + id);
        }
    }

}
