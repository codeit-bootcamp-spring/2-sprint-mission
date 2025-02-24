package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        User user = new User("Codeit");
        userService.createUser(user);
        System.out.println("Created user: " + user.getName());

        User retrievedUser = userService.readUser(user.getId());
        System.out.println("Retrieved user: " + retrievedUser.getName());

        user.update("Codeit Updated");
        userService.updateUser(user.getId(), user);
        retrievedUser = userService.readUser(user.getId());
        System.out.println("Updated user: " + retrievedUser.getName());

        userService.deleteUser(user.getId());
        retrievedUser = userService.readUser(user.getId());
        System.out.println("Deleted user: " + (retrievedUser == null ? "Yes" : "No"));

        Channel channel = new Channel("General");
        channelService.createChannel(channel);
        System.out.println("Created channel: " + channel.getChannelName());

        Channel retrievedChannel = channelService.readChannel(channel.getId());
        System.out.println("Retrieved channel: " + retrievedChannel.getChannelName());

        channel.update("General Updated");
        channelService.updateChannel(channel.getId(), channel);
        retrievedChannel = channelService.readChannel(channel.getId());
        System.out.println("Updated channel: " + retrievedChannel.getChannelName());

        channelService.deleteChannel(channel.getId());
        retrievedChannel = channelService.readChannel(channel.getId());
        System.out.println("Deleted channel: " + (retrievedChannel == null ? "Yes" : "No"));

        Message message = new Message("Hello codeit");
        messageService.createMessage(message);
        System.out.println("Created message: " + message.getMessage());

        Message retrievedMessage = messageService.readMessage(message.getId());
        System.out.println("Retrieved message: " + retrievedMessage.getMessage());

        message.update("Updated: Hello, Discord!");
        messageService.updateMessage(message.getId(), message);
        retrievedMessage = messageService.readMessage(message.getId());
        System.out.println("Updated message: " + retrievedMessage.getMessage());

        messageService.deleteMessage(message.getId());
        retrievedMessage = messageService.readMessage(message.getId());
        System.out.println("Deleted message: " + (retrievedMessage == null ? "Yes" : "No"));
    }
}
