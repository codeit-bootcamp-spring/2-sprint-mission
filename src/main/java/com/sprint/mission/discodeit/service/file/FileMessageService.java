package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final Path directory;
    private final UserService userservice;
    private final ChannelService channelservice;

    public FileMessageService(UserService userservice, ChannelService channelservice){
        this.directory = Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName())
                .toAbsolutePath().normalize();
        this.userservice = userservice;
        this.channelservice = channelservice;
        init();
    }

    private void init() {
        if (!Files.isDirectory(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void save(Message message){
        Path filePath = directory.resolve(message.getId() + ".ser");
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMessage(String message, UUID userId, UUID channelId) {
        Message newMessage = new Message(message, userId, channelId);
        save(newMessage);
    }

    @Override
    public Optional<Message> getOneMessage(UUID id){
        return getAllMessage().stream().filter(message -> message.getId().equals(id)).findFirst();
    }

    @Override
    public List<Message> getAllMessage(){
        try {
            return Files.list(directory)
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMessage(String message, UUID id){
        List<Message> messages = getAllMessage();
        for(Message msg : messages){
            msg.updateMessage(message);
            save(msg);
            break;
        }
    }

    @Override
    public void deleteMessage(UUID id){
        Path filePath = directory.resolve(id + ".ser");
        try{
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
