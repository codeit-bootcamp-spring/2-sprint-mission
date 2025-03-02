package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserRepository;

public class JCFmessageServiceImplement implements MessageService {
   private UserRepository users;
    private MessageRepository messagebox;
    //유저/메세지
    public JCFmessageServiceImplement(UserRepository users, MessageRepository messagebox) {
        this.messagebox=messagebox;//유저레포
        this.users = users;

    }

    @Override
    public void sendMessage(Message message) {      //대화창을 리스트로 구현
        if (!users.containsUser(message.getSender()))
            System.out.println("발신자 " + message.getSender() + "가 존재하지 않습니다.");
        else if (!users.containsUser(message.getReceiver()))
            System.out.println("수신자 " + message.getReceiver() + "가 존재하지 않습니다.");
        else {
            messagebox.addMessage(message);
            System.out.println("메시지를 보냈습니다.");
        }
    }
    @Override
    public void reciveMessage() {
    }

    @Override
    public void AllMessageView() {
        messagebox.allMessages();
    }


    @Override
    public void deleteMessage(Message  message) {
        if (users.containsUser(message.getSender())) {
            messagebox.deleteMessages(message);

        }
    }
}


