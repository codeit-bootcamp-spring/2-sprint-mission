package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

//메세지 저저장
public class JCFMessageRepositoryimplement implements MessageRepository {
    private List<Message> messagebox = new ArrayList<>();
    @Override
    public void  addMessage(Message message) {
        messagebox.add(message); //메세지넣기
        }

    @Override //특정인에게 보내거나 받은 메세지 조회
    public List<Message> getMessages(Message message) {
        return messagebox.stream().filter(m -> m.getSender().equals(message.getMessage()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> allMessages() {
        return messagebox;
    }
    @Override
    public List<Message> deleteMessages(Message message) {
        messagebox = messagebox.stream().filter(m -> m.getSender().equals(message.getMessage())).collect(Collectors.toList());
        return messagebox;

    }
}
