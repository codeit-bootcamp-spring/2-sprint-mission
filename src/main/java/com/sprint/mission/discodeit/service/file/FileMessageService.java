package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
    private final FileSerializationUtil fileUtil;
    private final UserService userService;
    private final ChannelService channelService;

    private static FileMessageService messageService;

    private FileMessageService(FileSerializationUtil fileUtil, UserService userService, ChannelService channelService) {
        this.fileUtil = fileUtil;
        this.userService = userService;
        this.channelService = channelService;
    }

    public static FileMessageService getInstance(FileSerializationUtil fileUtil, UserService userService, ChannelService channelService) {
        if (messageService == null) {
            messageService = new FileMessageService(fileUtil,userService , channelService);
        }
        return messageService;
    }
    @Override
    public void create(Message message, UUID channelId, UUID authorId) {
        userService
                .findById(authorId)
                .orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        channelService
                .findById(channelId)
                .orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        fileUtil.<Message>writeObjectToFile(message, FilePathUtil.getFilePath(directory, message.getId()));
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return fileUtil.<Message>readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public List<Message> findAll() {
        if (Files.exists(directory)) {
            try {
                return Files.list(directory)
                        .map(fileUtil::<Message>readObjectFromFile) // Optional<User> 반환
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            } catch (Exception e) {
                throw new RuntimeException("메시지 파일 읽기에 실패했습니다.", e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void update(UUID id, String content, UUID channelId, UUID authorId) {
        Message message = findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 메시지가 존재하지 않습니다: " + id));
        message.setUpdatedAt(System.currentTimeMillis());
        message.setContent(content);
        message.setChannelId(channelId);
        message.setAuthorId(authorId);

        fileUtil.<Message>writeObjectToFile(message, FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }
}
