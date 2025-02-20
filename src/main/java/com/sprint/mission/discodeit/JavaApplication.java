package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.*;
import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {
        // Initialize services
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // Create Users
        User user1 = new User("Alice");
        User user2 = new User("Bob");
        userService.createUser(user1);
        userService.createUser(user2);

        // Create Channels
        Channel channel1 = new Channel("General");
        channelService.createChannel(channel1);

        // Create Messages
        Message message1 = new Message("Hello, world!", user1.getId(), channel1.getId());
        messageService.createMessage(message1);

        // Read Users
        System.out.println("All Users: " + userService.getAllUsers());
        System.out.println("User1: " + userService.getUser(user1.getId()).getUsername());

        // Update User
        userService.updateUser(user1.getId(), "AliceUpdated");
        System.out.println("Updated User1: " + userService.getUser(user1.getId()).getUsername());

        // Delete User
        userService.deleteUser(user2.getId());
        System.out.println("All Users After Deletion: " + userService.getAllUsers());

        // Read Channels
        System.out.println("All Channels: " + channelService.getAllChannels());

        // Update Channel
        channelService.updateChannel(channel1.getId(), "UpdatedGeneral");
        System.out.println("Updated Channel: " + channelService.getChannel(channel1.getId()).getName());

        // Read Messages
        System.out.println("All Messages: " + messageService.getAllMessages());

        // Update Message
        messageService.updateMessage(message1.getId(), "Updated Message");
        System.out.println("Updated Message: " + messageService.getMessage(message1.getId()).getContent());

        // Delete Message
        messageService.deleteMessage(message1.getId());
        System.out.println("All Messages After Deletion: " + messageService.getAllMessages());
    }
}

// Commit and Tagging Instructions
// After implementing the main class and CRUD tests, commit the changes and create a tag:
// git add .
// git commit -m "Implement main application with CRUD test cases"
// git tag sprint1-main
