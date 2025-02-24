package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {

        JCFMessageService messageService = new JCFMessageService();
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();


        boolean running = true;
        while (running) {
            System.out.println("=====Main=====");
            System.out.println("1. Send Message");
            System.out.println("2. Display Message");
            System.out.println("3. Edit Message");
            System.out.println("4. Display Edit Message");
            System.out.println("5. Delete Message");
            System.out.println("6. Register User");
            System.out.println("7. Register Channel");
            System.out.println("0. Exit");
            System.out.print("=> ");


            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1: //메세지 보내기
                    //User user,String content,User receiver,UUID toChannelId
                    System.out.println("Sender name: ");
                    String sender = scanner.nextLine();

                    System.out.print("receiver name: ");
                    String receiver= scanner.nextLine();

                    System.out.print("Enter Message: ");
                    String content = scanner.nextLine();

                    System.out.print("Enter Channel: ");
                    String channel = scanner.nextLine();

                    Message message = new Message(userService.findByUserId(userService.findByUsername(sender)),content,userService.findByUserId(userService.findByUsername(receiver)),channelService.findChannelIdByName(channel));
                    messageService.sendMessage(message);
                    break;
                case 2:
                    System.out.println("full message or one message (1 or 2): ");
                    int c= scanner.nextInt();
                    scanner.nextLine();
                    if(c==1){
//                        System.out.println(messageService.findAllMessages());
                        System.out.println(messageService.createAllMessageContents());
                    }else if(c==2){
                        System.out.print("Enter Message ID: ");
                        String messageId = scanner.nextLine();
                        try{
                            UUID uuid = UUID.fromString(messageId);
                            System.out.println(messageService.findOneMessage(uuid));
                        } catch (IllegalArgumentException e){
                            System.out.println("Invalid Message ID");
                        }
                    }else{
                        System.out.print("Press 1 or 2");
                    }
                    break;
                case 3:
                    System.out.print("Enter Message ID: ");
                    String messageId2 = scanner.nextLine();

                    System.out.print("Enter Content: ");
                    String content2 = scanner.nextLine();
                    try {
                        UUID uuid2 = UUID.fromString(messageId2);
                        messageService.editMessage(uuid2,content2);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Message ID");
                    }
                    break;
                case 4:
                    //변경된 메세지 출력
                    System.out.println(messageService.displayEditmessages());
                    break;
                case 5:
                    //메세지 삭제
                    System.out.print("Enter Message ID: ");
                    String messageId3 = scanner.nextLine();

                    try{
                        UUID uuid3 = UUID.fromString(messageId3);
                        messageService.deleteMessage(uuid3);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Message ID");
                    }
                    break;
                case 6:
                    System.out.print("Enter User name: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Channel name: ");
                    String channelName = scanner.nextLine();
                    try{
                        UUID channelChoice= channelService.findChannelIdByName(channelName);
                        userService.createUser(username,channelService.findChannelById(channelChoice));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Channel Name");
                    }
                    break;
                case 7:
                    System.out.print("Enter Channel name: ");
                    String channelName2 = scanner.nextLine();

                    channelService.createChannel(channelName2);
                    break;
                case 0:
                    running = false;
                    break;

            }
        }
    }
}
