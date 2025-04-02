//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.ChannelType;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.*;
//
//public class JCFChannelService implements ChannelService {
//    private static JCFChannelService INSTANCE;
//    private final Map<UUID, Channel> channels = new HashMap<>();
//    private final UserService userService;
//
//    private JCFChannelService(UserService userService) {
//        this.userService = userService;
//    }
//
//    public static synchronized JCFChannelService getInstance(UserService userService) {
//        if (INSTANCE == null) {
//            INSTANCE = new JCFChannelService(userService);
//        }
//        return INSTANCE;
//    }
//
//    @Override
//    public Channel createChannel(ChannelType type, String channelName, String description) {
//        Channel channel = new Channel(type, description, channelName);
//        channels.put(channel.getId(), channel);
//        return channel;
//    }
//
//    @Override
//    public Channel findChannelById(UUID channelId) {
//        validateChannelExists(channelId);
//        return channels.get(channelId);
//    }
//
//    @Override
//    public String findChannelNameById(UUID channelId) {
//        validateChannelExists(channelId);
//        return channels.get(channelId).getChannelName();
//    }
//
//    @Override
//    public List<Channel> getAllChannels() {
//        return new ArrayList<>(channels.values());
//    }
//
//    @Override
//    public void updateChannelName(UUID channelId, String newChannelName) {
//        Channel channel = findChannelById(channelId);
//        channel.updateChannelName(newChannelName);
//    }
//
//    @Override
//    public void updateChannelDescription(UUID channelId, String newChannelDescription) {
//
//    }
//
//    @Override
//    public void updateChannelType(UUID channelId, ChannelType newChannelType) {
//
//    }
//
//    @Override
//    public void addUser(UUID channelId, UUID userId) {
//        Channel channel = findChannelById(channelId);
//
//        if (channel.isUserInChannel(userId)) {
//            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
//        }
//
//        userService.addChannel(userId, channelId);
//        channel.addMembers(userId);
//    }
//
//    @Override
//    public void addMessage(UUID channelId, UUID messageId) {
//        Channel channel = findChannelById(channelId);
//        channel.addMessages(messageId);
//    }
//
//    @Override
//    public void deleteChannel(UUID channelId) {
//        Channel channel = findChannelById(channelId);
//
//        for (UUID userId : channel.getMembers()) {
//            userService.removeChannel(userId, channelId);
//        }
//
//        channels.remove(channelId);
//    }
//
//    @Override
//    public void removeUser(UUID channelId, UUID userId) {
//        Channel channel = findChannelById(channelId);
//        if (!channel.isUserInChannel(userId)) {
//            throw new IllegalArgumentException("채널에 존재하지 않는 유저입니다.");
//        }
//
//        userService.removeChannel(userId, channelId);
//        channel.removeMember(userId);
//    }
//
//    @Override
//    public void removeMessage(UUID channelId, UUID messageId) {
//        Channel channel = findChannelById(channelId);
//        if (!channel.isMessageInChannel(messageId)) {
//            throw new IllegalArgumentException("체널에 존재하지 않는 메세지 입니다.");
//        }
//
//        channel.removeMessage(messageId);
//    }
//
//    public void validateChannelExists(UUID channelId) {
//        if (!channels.containsKey(channelId)) {
//            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
//        }
//    }
//}
