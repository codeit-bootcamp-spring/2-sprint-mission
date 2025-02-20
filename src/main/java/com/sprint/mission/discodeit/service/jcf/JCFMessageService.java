package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    public final List<Message> data = new ArrayList<>();

    @Override
    public void sendMessage(User user, Channel channel, String content) {
        Message message = new Message(user, channel ,content);
        data.add(message);
        System.out.println("메세지 전송 성공" + message);
    }

    @Override
    public void findMessageById(UUID id) {
        if(data.stream().noneMatch(message -> message.getId().equals(id))){
            System.out.println("[실패]입력 메시지가 존재하지 않습니다.");
            return;
        }

        for(Message message:data){
            if(message.getId().equals(id)){
                System.out.println(message);
                return;
            }
        }
    }

    @Override
    public void findAllMessages() {
        if(data.isEmpty()){
            System.out.println("입력 메시지가 존재하지 않습니다.");
            return;
        }

        data.forEach(message -> System.out.println(message.toString()));
    }

    @Override
    public void updateMessage(UUID id, String content) {
        for(Message message : data) {
            if(message.getId().equals(id)) {
                message.update(content);
                System.out.println("[성공]메시지 변경 완료[메시지 아이디: " + message.getId() +
                        ", 닉네임: " + message.getUser().getNickname() +
                        ", 채널명: " + message.getChannel().getChannelName() +
                        ", 변경 시간: " + message.getUpdatedAt() +
                        "]");
            }
        }
    }

    @Override
    public void deleteMessageById(UUID id) {
        boolean isremove =  data.removeIf(message -> message.getId().equals(id));

        if(!isremove){
            System.out.println("[실패]삭제하려는 메시지가 존재하지 않습니다.");
        } else {
            System.out.println("[성공]메세지 삭제 완료");
        }
    }
}
