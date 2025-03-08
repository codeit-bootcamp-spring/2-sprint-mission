package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;
    private final ChannelService channelService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    private JCFMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        if (instance == null) {
            synchronized (JCFMessageService.class) {
                if (instance == null) {
                    instance = new JCFMessageService(userService, channelService, messageRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public void createMessage(Message message) {
        //데이터 유효성 확인
        messageValidationCheck(message);
        //요청자 확인
        User user = findUserOrThrow(message.getUserId());
        //채널 확인
        Channel channel = findChannelOrThrow(message.getChannelId());
        //작성 권한 확인
        validateUserPermission(user, null, channel.getWritePermission(), ActionType.WRITE);
        //create
        messageRepository.createMessage(message);
    }

    @Override
    public Optional<Message> selectMessageById(UUID id) {
        return messageRepository.selectMessageById(id);
    }

    @Override
    public List<Message> selectMessagesByChannel(UUID channelId) {
        return messageRepository.selectMessagesByChannel(channelId);
    }

    @Override
    public void updateMessage(UUID id, String content, UUID userId, UUID channelId) {
        //요청자 확인
        User user = findUserOrThrow(userId);
        //메시지 유효성 확인
        Message message = findMessageOrThrow(id);
        //채널 확인
        ensureMessageBelongsToChannel(message, channelId);
        //수정 권한 확인
        validateUserPermission(user, message.getUserId(), null, ActionType.EDIT);
        //update
        if (content != null) {
            messageRepository.updateMessage(id, content, userId, channelId);
        }
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        //요청자 확인
        User user = findUserOrThrow(userId);
        //메시지 유효성 확인
        Message message = findMessageOrThrow(id);
        //채널 확인
        ensureMessageBelongsToChannel(message, channelId);
        //삭제 권한 확인
        validateUserPermission(user, message.getUserId(), null, ActionType.DELETE);
        //delete
        messageRepository.deleteMessage(id, userId, channelId);
    }

    /*******************************
     * Validation check
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

    private void ensureMessageBelongsToChannel(Message message, UUID channelId){
        if (!message.getChannelId().equals(channelId)) {
            throw new RuntimeException("해당 메시지는 요청한 채널에 속하지 않습니다.");
        }
    }

    private void validateUserPermission(User user, UUID messageUserId, UserRole channelRequiredRole, ActionType action) {
        if (action == ActionType.WRITE) {
            if (user.getRole() != channelRequiredRole && user.getRole() != UserRole.ADMIN) {
                throw new RuntimeException("메시지를 작성할 권한이 없습니다.");
            }
        } else {
            if (!user.getId().equals(messageUserId) && user.getRole() != UserRole.ADMIN) {
                throw new RuntimeException(String.format("메시지를 %s할 권한이 없습니다.", action == ActionType.EDIT ? "수정" : "삭제"));
            }
        }
    }

    private Message findMessageOrThrow(UUID messageId){
        return selectMessageById(messageId)
                .orElseThrow(() -> new RuntimeException("해당 메시지가 존재하지 않습니다. : " + messageId));
    }

    private Channel findChannelOrThrow(UUID channelId){
        return channelService.selectChannelById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널이 존재하지 않습니다. : " + channelId));
    }

    private User findUserOrThrow(UUID userId) {
        return userService.selectUserById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다. : " + userId));
    }
}
