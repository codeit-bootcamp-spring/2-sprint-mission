package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final Path directory;

    public FileChannelService() {
        this.directory = Paths.get(System.getProperty("user.dir"), "file-data-map", Channel.class.getSimpleName())
                        .toAbsolutePath().normalize();
        init();
    }

    private void init() {
        if(!Files.exists(directory)){
            try{
                Files.createDirectories(directory);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void save(Channel channel) {
        Path filePath = directory.resolve(channel.getId() + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createChannel(String name, String topic){
        Channel newChannel = new Channel(name, topic);
        save(newChannel);
    }

    @Override
    public Optional<Channel> getOneChannel(UUID id){
        return getAllChannel().stream().filter(channel -> channel.getId().equals(id)).findFirst();
    }

    @Override
    public List<Channel> getAllChannel(){
        List<Channel> channels = new ArrayList<>();
        if(Files.exists(directory)) {
            try {
                Files.list(directory).forEach(path -> {
                    try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                        channels.add((Channel) ois.readObject());
                    } catch (IOException | ClassNotFoundException e){
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        return channels;
    }

    @Override
    public void updateChannel(String newname, String newtopic, UUID id){
        List<Channel> channels = getAllChannel();
        for(Channel channel : channels){
            if(channel.getId().equals(id)){
                channel.updateChannel(newname, newtopic);
                save(channel);
                break;
            }
        }
    }

    @Override
    public void deleteChannel(UUID id){
        Path filePath = directory.resolve(id + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
