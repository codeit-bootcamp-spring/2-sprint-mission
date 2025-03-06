package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileMessageRepository implements MessageRepository {
    Path directory = Paths.get(System.getProperty("user.dir"),
            "src/main/java/com/sprint/mission/discodeit/data/Message");

    private boolean fileExists(UUID messageId) {
        return !Files.exists(directory.resolve(messageId + ".ser"));
    }

    private void save(UUID messgaeId, Message message) {
        Path filePath = directory.resolve(messgaeId + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패" + e.getMessage());
        }
    }

    private List<Message> loadAllMessagesFile() {
        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("디렉토리가 생성되어 있지 않음.");
        }
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.map(path -> {
                try (
                        FileInputStream fis = new FileInputStream(path.toFile());
                        ObjectInputStream ois = new ObjectInputStream(fis)
                ) {
                    return (Message) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("파일 로드 실패: " + e.getMessage(), e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("파일 로드 실패" + e.getMessage());
        }
    }

    private void deleteMessageFile(UUID messageId) {
        Path filePath = directory.resolve(messageId + ".ser");
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 불가" + e.getMessage());
            }
        }
    }

    @Override
    public Message findById(UUID messageId) {
        if (fileExists(messageId)) {
            throw new IllegalArgumentException("존재하지 않는 메시지ID입니다.");
        }
        Path filePath = directory.resolve(messageId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패: " + e.getMessage());
        }
    }

    @Override
    public List<Message> findAll() {
        return Optional.ofNullable(loadAllMessagesFile())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<Message> findUpdatedMessages() {
        return loadAllMessagesFile().stream()
                .filter(channel -> channel.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createMessage(Channel channel, User user, String messageContent) {
        Message message = new Message(channel, user, messageContent);
        save(message.getId(), message);
    }

    @Override
    public void updateMessage(UUID messageId, String messageContent) {
        if (fileExists(messageId)) {
            throw new IllegalArgumentException();
        }
        Message message = findById(messageId);
        deleteMessageFile(message.getId());
        message.messageUpdate(messageContent);
        save(message.getId(), message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        if (fileExists(messageId)) {
            throw new IllegalArgumentException("존재하지 않는 메시지ID입니다.");
        }
        deleteMessageFile(messageId);
    }

    @Override
    public void deleteMessagesByChannelId(UUID channelId) {
        List<UUID> removeId = loadAllMessagesFile().stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .map(Message::getId)
                .toList();
        removeId.forEach(this::deleteMessageFile);
    }

}
