package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "channels");
    private final FileSerializationUtil fileUtil;
    private static FileChannelRepository channelRepository;


    private FileChannelRepository(FileSerializationUtil fileUtil){
        this.fileUtil = fileUtil;
    }

    public static FileChannelRepository getInstance(FileSerializationUtil fileUtil){
        if(channelRepository == null){
            channelRepository = new FileChannelRepository(fileUtil);
        }

        return channelRepository;
    }


    @Override
    public void save(Channel channel) {
        fileUtil.<Channel>writeObjectToFile(channel, FilePathUtil.getFilePath(directory, channel.getId()));
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return fileUtil.<Channel>readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<List<Channel>> findAll() {
        try {
            List<Channel> channel = Files.list(directory)
                    .map(fileUtil::<Channel>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return Optional.of(channel);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Channel channel) {
        save(channel);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }
}
