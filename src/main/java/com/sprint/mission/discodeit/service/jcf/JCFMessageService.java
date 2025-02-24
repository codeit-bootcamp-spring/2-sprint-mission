package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final List<Message> data = new ArrayList<>();
    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfChannelService;
    private static JCFMessageService getInstance;

    private JCFMessageService(JCFUserService jcfUserService, JCFChannelService jcfChannelService) {
        this.jcfUserService = jcfUserService;
        this.jcfChannelService = jcfChannelService;
    }

    public static JCFMessageService getInstance(JCFUserService jcfUserService, JCFChannelService jcfChannelService){
        if(getInstance == null){
            getInstance = new JCFMessageService(jcfUserService, jcfChannelService);
        }
        return getInstance;
    }

    @Override
    public void sendMessage(UUID channelId, UUID userId, String content) {

        if(jcfChannelService.findChannel(channelId) ==null){
            return;
        }

        if(jcfUserService.findByUser(userId) == null){
            return;
        }

        Message message = new Message(jcfChannelService.findChannel(channelId), jcfUserService.findByUser(userId) ,content);
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
    public void findMessageByChannelId(UUID channelId) {
        if(jcfChannelService.findChannel(channelId) ==null){
            return;
        }

        if(data.stream().noneMatch(message -> message.getChannel().getId().equals(channelId))){
            System.out.println("채널에 해당하는 메시지가 존재하지 않습니다.");
            return;
        }

        for(Message message:data){
            if(message.getChannel() == jcfChannelService.findChannel(channelId)){
                System.out.println(message);
            }
        }
    }

    @Override
    public void updateMessage(UUID id, String content) {
        if(data.stream().noneMatch(data -> data.getId().equals(id))) {
            System.out.println("[실패]수정하려는 메세지가 존재하지 않습니다.");
            return;
        }

        for(Message message : data) {
            if(message.getId().equals(id)) {
                message.update(content);
                System.out.println("[성공]메시지 변경 완료[메시지 아이디: " + message.getId() +
                        ", 닉네임: " + message.getUser().getNickname() +
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
