package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.constants.FilePath.MESSAGE_FILE;
import static com.sprint.mission.discodeit.constants.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final UserService userService;

    public FileMessageService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public MessageDto create(String context, UUID channelId, UUID userId) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());
        Message message = new Message(context, channelId, userId);
        messages.put(message.getId(), message);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), MESSAGE_FILE.getPath(), messages);

        return toDto(message);
    }

    @Override
    public MessageDto findById(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());
        Message message = messages.get(id);
        if (message == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent());
        }

        return toDto(message);
    }

    @Override
    public List<MessageDto> findAll() {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());

        return messages.values()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<MessageDto> findByChannelId(UUID channelId) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());

        return messages.values()
                .stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(this::toDto)
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());
        messages.get(id)
                .updateContext(context);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), MESSAGE_FILE.getPath(), messages);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());
        messages.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), MESSAGE_FILE.getPath(), messages);
    }

    private MessageDto toDto(Message message) {
        UserDto user = userService.findById(message.getUserId());
        return new MessageDto(message.getId(), message.getContext(), message.getChannelId(), user);
    }
}
