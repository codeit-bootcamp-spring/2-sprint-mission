package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        User user = new User("Codeit");
        userService.createUser(user);
        System.out.println("Created user: " + user.getName());

        Channel channel = new Channel("General");
        channelService.createChannel(channel);
        System.out.println("Created channel: " + channel.getChannelName());

        Message message = messageService.createMessage(user.getId(), channel.getId(), "Hello Codeit");
        System.out.println("Created message: " + message.getMessage());

        Message retrievedMessage = messageService.readMessage(message.getId());
        System.out.println("Retrieved message: " + retrievedMessage.getMessage());

        message.update("Updated: Hello, Sprint!");
        messageService.updateMessage(message.getId(), message);
        retrievedMessage = messageService.readMessage(message.getId());
        System.out.println("Updated message: " + retrievedMessage.getMessage());

        List<Message> allMessages = messageService.readAllMessages();
        System.out.println("All messages:");
        for (Message msg : allMessages) {
            System.out.println(msg.getMessage());
        }

        messageService.deleteMessage(message.getId());
        retrievedMessage = messageService.readMessage(message.getId());
        System.out.println("Deleted message: " + (retrievedMessage == null ? "Yes" : "No"));

        User retrievedUser = userService.readUser(user.getId());
        System.out.println("Retrieved user: " + retrievedUser.getName());

        user.update("Codeit Updated");
        userService.updateUser(user.getId(), user);
        retrievedUser = userService.readUser(user.getId());
        System.out.println("Updated user: " + retrievedUser.getName());

        Channel retrievedChannel = channelService.readChannel(channel.getId());
        System.out.println("Retrieved channel: " + retrievedChannel.getChannelName());

        channel.update("General Updated");
        channelService.updateChannel(channel.getId(), channel);
        retrievedChannel = channelService.readChannel(channel.getId());
        System.out.println("Updated channel: " + retrievedChannel.getChannelName());

        userService.deleteUser(user.getId());
        retrievedUser = userService.readUser(user.getId());
        System.out.println("Deleted user: " + (retrievedUser == null ? "Yes" : "No"));


        channelService.deleteChannel(channel.getId());
        retrievedChannel = channelService.readChannel(channel.getId());
        System.out.println("Deleted channel: " + (retrievedChannel == null ? "Yes" : "No"));

    }
}
