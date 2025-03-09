package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    public FileMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }
    /*
    private static volatile FileMessageService instance;
    public static FileMessageService getInstance(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        if(instance == null) {
            synchronized (FileMessageService.class) {
                if(instance == null) {
                    instance = new FileMessageService(userService, channelService, messageRepository);
                }
            }
        }
        return instance;
    }
    */

    @Override
    public void createMessage(Message message) {
        //요청자 확인
        User user = findUserOrThrow(message.getUserId());
        //채널 확인
        Channel channel = findChannelOrThrow(message.getChannelId());
        //작성 권한 확인
        validateWritePermission(user, channel.getWritePermission());
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
        validateEditDeletePermission(user, message.getUserId());
        //update
        messageRepository.updateMessage(id, content, userId, channelId);
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
        validateEditDeletePermission(user, message.getUserId());
        //delete
        messageRepository.deleteMessage(id, userId, channelId);
    }

    /*******************************
     * Validation check
     *******************************/
    private void ensureMessageBelongsToChannel(Message message, UUID channelId){
        if (!message.getChannelId().equals(channelId)) {
            throw new RuntimeException("해당 메시지는 요청한 채널에 속하지 않습니다.");
        }
    }

    private void validateWritePermission(User user, UserRole channelRequiredRole) {
        if (user.getRole() != channelRequiredRole && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("메시지를 작성할 권한이 없습니다.");
        }
    }

    private void validateEditDeletePermission(User user, UUID messageUserId) {
        if (!user.getId().equals(messageUserId) && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("메시지를 수정/삭제할 권한이 없습니다.");
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