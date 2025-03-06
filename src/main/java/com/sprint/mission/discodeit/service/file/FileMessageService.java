package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private final FileMessageRepository fileMessageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(FileMessageRepository fileMessageRepository, ChannelService channelService, UserService userService) {
        this.fileMessageRepository = fileMessageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        try {
            channelService.findById(channelId);
            userService.findById(userId);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("유효하지 않은 채널 ID 또는 유저 ID입니다.", e);
        }
        Message message = new Message(content, channelId, userId);
        fileMessageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return fileMessageRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Message findById(UUID messageId) {
        return fileMessageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = fileMessageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
        message.update(newContent);
        fileMessageRepository.save(message);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!fileMessageRepository.delete(messageId)) {
            throw new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId);
        }
    }
}
