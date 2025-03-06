package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<>();
    private final FileUserService fileUserService;
    private final FileChannelService fileChannelService;

    public FileMessageService(FileUserService fileUserService, FileChannelService fileChannelService) {
        this.fileUserService = fileUserService;
        this.fileChannelService = fileChannelService;
        loadFromFile("messages.ser");
    }

    @Override
    public void create(Message message) {
        User sender = message.getSender();
        Channel channel = message.getChannel();

        if (fileUserService.find(sender.getId()) == null) {
            System.out.println("유저가 존재하지 않습니다.");
            return;
        }

        if (fileChannelService.find(channel.getId()) == null) {
            System.out.println("채널이 존재하지 않습니다.");
            return;
        }

        if (!channel.isMember(sender)) {
            System.out.println("유저가 채널에 등록되어 있지 않습니다.");
            return;
        }

        messages.put(message.getId(), message);
        saveInFile(messages, "messages.ser");
    }

    @Override
    public Message find(UUID id) {
        return messages.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void update(Message message) {
        User sender = message.getSender();
        Channel channel = message.getChannel();

        if (fileUserService.find(sender.getId()) == null) {
            System.out.println("유저가 존재하지 않습니다.");
            return;
        }

        if (fileChannelService.find(channel.getId()) == null) {
            System.out.println("채널이 존재하지 않습니다.");
            return;
        }

        messages.put(message.getId(), message);
        saveInFile(messages, "messages.ser");
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
        saveInFile(messages, "messages.ser");
    }

    public static void saveInFile(Map<UUID, Message> messages, String fileName){
        try(FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(messages);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Map<UUID, Message> loadFromFile(String fileName){
        Map<UUID, Message> messages = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
