package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName()).toAbsolutePath();
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.userService = userService;
        this.channelService = channelService;
    }

    public Path getFilePath(UUID messageId) {
        return DIRECTORY.resolve("message-" + messageId + EXTENSION);
    }

    private void serializeMessage(Message message, Path path) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("사용자 데이터를 저장하는 중 오류 발생: " + path, e);
        }
    }

    private Message deserialMessage(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 파일을 읽는 중 오류 발생: " + path, e);
        }
    }

    @Override
    public Message createMessage(String text, UUID userId, UUID channelId) {
        try {
            userService.searchUser(userId);
        } catch (Exception e) {
            throw new NoSuchElementException("존재하지 않는 사용자 입니다. 메세지를 생성할 수 없습니다.");
        }

        try {
            channelService.searchChannel(channelId);
        } catch (Exception e) {
            throw new NoSuchElementException("존재하지 않는 채널 입니다. 메세지를 생성할 수 없습니다.");
        }
        Message message = new Message(text, userId, channelId);
        serializeMessage(message, getFilePath(message.getId()));
        System.out.println("메세지가 생성되었습니다: \n" + message);
        return message;
    }

    @Override
    public Message searchMessage(UUID messageId) {
        return deserialMessage(getFilePath(messageId));
    }

    @Override
    public List<Message> searchAllMessages() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserialMessage(path))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message updateMessage(UUID messageId, String text) {
        existMessage(messageId);
        Path path = getFilePath(messageId);
        Message message = deserialMessage(path);
        message.updateText(text);

        serializeMessage(message, path);
        return message;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        existMessage(messageId);
        Path path = getFilePath(messageId);
        try {
            Files.delete(path);
            System.out.println(messageId + " 메세지 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("메세지 삭제 실패: " + messageId, e);
        }
    }

    public boolean existMessage(UUID messageId) {
        Path path = getFilePath(messageId);
        if (!Files.exists(path)) {
            throw new NoSuchElementException("메세지가 존재하지 않습니다.");
        }
        return true;
    }
}