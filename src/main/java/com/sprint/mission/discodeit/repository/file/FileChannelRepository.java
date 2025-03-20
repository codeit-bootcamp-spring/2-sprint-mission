package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            Channel.class.getSimpleName());

    public FileChannelRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public Channel save(Channel channel) {
        return FileUtil.save(DIRECTORY, channel, channel.getId());
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id, Channel.class);
    }

    @Override
    public List<Channel> findAll() {
        return FileUtil.findAll(DIRECTORY, Channel.class);
    }

    @Override
    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }

    @Override
    public void deleteById(UUID id) {
        FileUtil.delete(DIRECTORY, id);
    }
}
