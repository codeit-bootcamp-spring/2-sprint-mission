package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private List<Channel> data = loadFromFile();
    private final UserService userService; // FileUserService의 메소드를 써야하므로..


    public FileChannelService(UserService userService) {
        this.userService = userService;
    }

    private static ChannelService channelService;

    public static ChannelService getInstance(UserService userService) {
        if (channelService == null) {
            channelService = new FileChannelService(userService);
        }
        return channelService;
    }



    // =================================== 채널 생성 ===================================
    @Override
    public void createChannel(Channel channel) {
        validateCreateChannel(channel);
        checkDuplicateChannelName(channel.getChannelName());
        saveToFile(channel);
    }

    private void validateCreateChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("생성하려는 채널 정보가 존재하지 않습니다.");
        }
        if (channel.getChannelName() == null || channel.getChannelName().isBlank() || channel.getOwner() == null) {
            throw new IllegalArgumentException("필수 입력 항목이 입력되지 않았습니다. (channelName, owner)");
        }
    }

    private void checkDuplicateChannelName(String channelName) {
        if (data.stream().anyMatch(ch -> ch.getChannelName().equals(channelName))) {
            throw new IllegalStateException("중복된 channelName 입니다.");
        }
    }


    // =================================== 채널 조회 ===================================
    @Override
    public Channel getChannel(String channelName) {
        if (channelName == null || channelName.isBlank()) {
            throw new IllegalArgumentException("channelName이 입력되지 않았습니다.");
        }

        Channel findChannel = data.stream()
                .filter(ch -> ch.getChannelName().equals(channelName))
                .findFirst() //
                .orElseThrow(() -> new NoSuchElementException("해당 channelName을 가진 채널은 존재하지 않습니다."));

        return findChannel;
    }

    @Override
    public List<Channel> getAllChannels() {
        return data;
    }


    // =================================== 채널 수정 ===================================
    @Override
    public void addUsersToChannel(User requestUser, User user, String channelName) {
        Channel findChannel = getChannel(channelName);
        validateUpdateChannelAndUser(requestUser, user, findChannel);
        updateChannelInfo(findChannel);
        findChannel.addUsers(user);
        user.addChannel(findChannel);

        saveToFile(findChannel);
        userService.saveToFile(user); // 연관관계에 따라 유저 파일도 수정
    }

    @Override
    public void removeUsersFromChannel(User requestUser, User user, String channelName) {
        Channel findChannel = getChannel(channelName);
        validateUpdateChannelAndUser(requestUser, user, findChannel);
        updateChannelInfo(findChannel);
        findChannel.removeUsers(user);
        saveToFile(findChannel);

        user.removeChannel(findChannel);
        deleteUserMessagesFromChannel(findChannel, user);
        userService.saveToFile(user); // 연관관계에 따라 유저 파일도 수정
    }

    private void validateUpdateChannelAndUser(User requestUser, User user, Channel channel) {
        if (user == null) {
            throw new IllegalArgumentException("수정하려는 유저 정보가 입력되지 않았습니다.");
        }

        if (!requestUser.getUsername().equals(channel.getOwner().getUsername())) {
            throw new IllegalStateException("수정할 채널의 주인이 아닙니다.");
        }
    }

    private void updateChannelInfo(Channel channel) {
        channel.updateUpdatedAt(System.currentTimeMillis());
    }

    private void deleteUserMessagesFromChannel(Channel channel, User user) {
        List<Message> findMessages = user.getMessages().stream().filter(m -> m.getChannel().equals(channel)).collect(Collectors.toList());
        findMessages.stream().forEach(m -> m.getSender().deleteMessage(m));
    }


    // =================================== 채널 삭제 ===================================
    // 채널 삭제
    @Override
    public void deleteChannel(User requestUser, String channelName) {
        Channel findChannel = getChannel(channelName);
        checkChannelOwner(requestUser, findChannel);
        deleteUserChannelAndMessage(findChannel);

        deleteFile(findChannel);
    }

    public void checkChannelOwner(User user, Channel channel) {
        if (!user.getUsername().equals(channel.getOwner().getUsername())) {
            throw new IllegalStateException("삭제할 채널의 주인이 아닙니다.");
        }
    }

    public void deleteUserChannelAndMessage(Channel channel) {
        List<User> users = channel.getUsers().stream().collect(Collectors.toList());
        users.stream().forEach(u -> u.removeChannel(channel));

        List<Message> messages = channel.getMessages(); // 해당 채널의 메시지들을 찾고,
        users.stream().forEach(u -> messages.stream().forEach(m -> u.deleteMessage(m)));  // 해당 유저에서 메시지를 삭제

        // User 리스트를 순회하면서 파일 수정
        users.stream().forEach(u -> userService.saveToFile(u));
    }


    // =================================== 직렬화 관련 메소드 ===================================
    @Override
    public void saveToFile(Channel channel) {
        Path directory = Paths.get(System.getProperty("user.dir"),"data", "channel");
        Path filePath = directory.resolve(channel.getChannelName() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, channel);
        data = loadFromFile();
    }

    @Override
    public List<Channel> loadFromFile() {
        Path directory = Paths.get(System.getProperty("user.dir"),"data", "channel");
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFile(Channel channel) {
        Path directory = Paths.get(System.getProperty("user.dir"),"data", "channel");
        Path filePath = directory.resolve(channel.getChannelName() + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("채널 파일 삭제 실패: " + e.getMessage());
        }
        data = loadFromFile();
    }
}
