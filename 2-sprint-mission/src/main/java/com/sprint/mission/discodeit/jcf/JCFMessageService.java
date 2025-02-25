package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static JCFMessageService instance;
    private final Map<UUID, Message> data;
    private final ChannelService channelService;
    private final UserService userService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            instance = new JCFMessageService(userService, channelService);
        }
        return instance;
    }

    @Override
    public void createMessage(Message message) {
        //데이터 유효성 확인
        this.messageValidationCheck(message);
        //요청자 확인
        User user = userService.selectUserById(message.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다.: " + message.getUserId()));
        //채널 확인, 채널 권한 확인
        Channel channel = channelService.selectChannelById(message.getChannelId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 채널이 존재하지 않습니다.: " + message.getChannelId()));
        if (user.getRole() != UserRole.ADMIN && user.getRole() != channel.getWritePermission()) {
            throw new RuntimeException("메시지를 작성할 권한이 없습니다.");
        }
        //create
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> selectMessageById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> selectMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content, UUID userId, UUID channelId) {
        //요청자 확인
        User user = userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + userId));
        //메시지 유효성 확인
        Message message = this.selectMessageById(id)
                .orElseThrow(() -> new RuntimeException("해당 메시지가 존재하지 않습니다. : " + id));
        //채널 확인
        if (!message.getChannelId().equals(channelId)) {
            throw new RuntimeException("해당 메시지는 요청한 채널에 속하지 않습니다.");
        }
        //수정 권한 체크
        if (!user.getId().equals(message.getUserId()) && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("메시지를 수정할 권한이 없습니다.");
        }
        //update
        if (content != null) {
            message.updateContent(content);
        }
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        //요청자 확인
        User user = userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + userId));
        //메시지 유효성 확인
        Message message = this.selectMessageById(id)
                .orElseThrow(() -> new RuntimeException("해당 메시지가 존재하지 않습니다. : " + id));
        //채널 확인
        if (!message.getChannelId().equals(channelId)) {
            throw new RuntimeException("해당 메시지는 요청한 채널에 속하지 않습니다.");
        }
        //삭제 권한 체크
        if (!user.getId().equals(message.getUserId()) && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("메시지를 삭제할 권한이 없습니다.");
        }
        //delete
        data.remove(id);
    }

    /*******************************
     * Message Data Validation check
     *******************************/
    private void messageValidationCheck(Message message){
        // 1. null check
        if (message == null) {
            throw new IllegalArgumentException("message 객체가 null입니다.");
        }
        if (message.getId() == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        if (message.getContent() == null) {
            throw new IllegalArgumentException("메시지 내용이 null입니다.");
        }
        if (message.getUserId() == null) {
            throw new IllegalArgumentException("채널 개설자의 ID가 없습니다.");
        }
        if (message.getChannelId() == null) {
            throw new IllegalArgumentException("채널의 ID가 없습니다.");
        }
    }
}
