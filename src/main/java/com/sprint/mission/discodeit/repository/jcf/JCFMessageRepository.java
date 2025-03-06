package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private volatile static JCFMessageRepository instance = null;
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    public static JCFMessageRepository getInstance(){
        if(instance == null){
            synchronized (JCFMessageRepository.class){
                if(instance == null){
                    instance = new JCFMessageRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID id) {
        return Optional.ofNullable(data.get(id)).orElseThrow(()-> new NoSuchElementException("Message not found"));
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
