package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private volatile static FileMessageService instance = null;
    private final UserService userService;
    private final ChannelService channelService;
    private static final String FILE_PATH = "message.dat";
    private final Map<UUID, Message> messageRepository;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.messageRepository = loadAll();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static FileMessageService getInstance(UserService userService, ChannelService channelService) {
        if(instance == null){
            synchronized (FileMessageService.class){
                if(instance == null){
                    instance = new FileMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    private Map<UUID, Message> loadAll(){
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (Map<UUID, Message>) ois.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(messageRepository);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Message saveMessage(Channel channel, User user, String text) {
        if(channelService.findById(channel.getId()).isEmpty()){
            throw new NoSuchElementException("not found channel");
        }
        if(userService.findById(user.getId()).isEmpty()){
            throw new NoSuchElementException("not found user");
        }

        Message message = new Message(channel.getId(), user.getId(), text);
        messageRepository.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public List<Message> findAll() {
        if(messageRepository.isEmpty()){
            throw new NoSuchElementException("메시지가 없습니다.");
        }

        return messageRepository.values().stream().toList();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageRepository.get(id));
    }

    @Override
    public void update(UUID id, String text) {
        if(!messageRepository.containsKey(id)){
            throw new NoSuchElementException("메시지가 없습니다.");
        }
        if(text == null){
            throw new IllegalArgumentException("수정할 내용은 null일 수 없습니다.");
        }

        messageRepository.get(id).setText(text);
        saveToFile();
    }

    @Override
    public void delete(UUID id) {
        messageRepository.remove(id);
        saveToFile();
    }
}
