package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.channel.ChannelByIdResponse;
import com.sprint.mission.discodeit.service.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.channel.PrivateChannelParam;
import com.sprint.mission.discodeit.service.dto.channel.PublicChannelParam;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateParam;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    private final ReadStatusService readStatusService;
    private final MessageRepository messageRepository;

    public FileChannelService(ReadStatusService readStatusService, MessageRepository messageRepository) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Channel.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.readStatusService = readStatusService;
        this.messageRepository = messageRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description, List<UUID> privateUserIds) {
        return switch (type) {
            case PRIVATE -> createPrivate(new PrivateChannelParam(type, privateUserIds));
            case PUBLIC -> createPublic(new PublicChannelParam(type, name, description));
        };
    }

    @Override
    public ChannelByIdResponse find(UUID channelId) {
        Channel channelNullable = null;
        Path path = resolvePath(channelId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                channelNullable = (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        Instant lastMessageTime = findLastMessageTime(channel.getId());

        if (channel.getType() == ChannelType.PRIVATE) {
            return new ChannelByIdResponse(lastMessageTime, channel,
                    readStatusService.findAllUserByChannelId(channelId));
        }
        return new ChannelByIdResponse(lastMessageTime, channel, null);
    }

    @Override
    public List<ChannelByIdResponse> findAllByUserId(UUID userId) {
        return findAll().stream()
                .map(channel -> {
                    Instant lastMessageTime = findLastMessageTime(channel.getId());
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return new ChannelByIdResponse(lastMessageTime, channel, null);
                    } else {
                        List<UUID> userIdsInChannel = readStatusService.findAllUserByChannelId(
                                channel.getId());
                        if (userIdsInChannel.contains(userId)) {
                            return new ChannelByIdResponse(lastMessageTime, channel, userIdsInChannel);
                        } else {
                            return null;
                        }
                    }
                }).filter(Objects::nonNull).toList();
    }


    @Override
    public Channel update(ChannelUpdateRequest updateRequest) {
        Channel channelNullable = null;
        Path path = resolvePath(updateRequest.id());
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                channelNullable = (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + updateRequest.id() + " not found"));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정 불가능");
        }

        channel.update(updateRequest.newName(), updateRequest.newDescription());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Path path = resolvePath(channelId);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        try {
            messageRepository.findAllByChannelId(channelId)
                    .forEach(message -> messageRepository.deleteById(message.getId()));
            readStatusService.findAllByChannelId(channelId).forEach(readStatusService::delete);
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    private Channel createPrivate(PrivateChannelParam privateParam) {
        Channel channel = new Channel(privateParam.type(), null, null);
        readStatusService.create(new ReadStatusCreateParam(privateParam.userIds(), channel.getId()));
        Path path = resolvePath(channel.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return channel;
    }

    private Channel createPublic(PublicChannelParam publicParam) {
        Channel channel = new Channel(publicParam.type(), publicParam.name(), publicParam.description());
        Path path = resolvePath(channel.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return channel;
    }

    private List<Channel> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (Channel) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Instant findLastMessageTime(UUID channelId) {
        List<Message> messageList = messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId)).toList();
        if (messageList.isEmpty()) {
            return null;
        }
        return messageList.stream().map(Message::getCreatedAt).max(Instant::compareTo).orElse(null);
    }
}
