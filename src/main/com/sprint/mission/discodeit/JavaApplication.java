package main.com.sprint.mission.discodeit;

import main.com.sprint.mission.discodeit.entity.Channel;
import main.com.sprint.mission.discodeit.entity.Message;
import main.com.sprint.mission.discodeit.entity.User;
import main.com.sprint.mission.discodeit.service.ChannelService;
import main.com.sprint.mission.discodeit.service.MessageService;
import main.com.sprint.mission.discodeit.service.UserService;
import main.com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import main.com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import main.com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

public class JavaApplication {
    // find user
    public UUID checkUser(UserService userServ, String name) {
        List<User> temp = userServ.getAllUser();
        for (User eachuser : temp)
            if (eachuser.getUsername().equals(name))
                return eachuser.getId();
        return null;
    }
    // find channel
    public UUID checkChannel(ChannelService channelServ, String name) {
        List<Channel> temp = channelServ.getAllChannel();
        for(Channel eachchannel : temp){
            if(eachchannel.getName().equals(name)){
                return eachchannel.getId();
            }
        }
        return null;
    }
    // find message
    public UUID checkMessage(MessageService messageServ, UUID userid, UUID channelid) {
        List<Message> temp = messageServ.getAllMessage();
        for(Message eachmessage : temp){
            if(eachmessage.getUser() == userid){
                if(eachmessage.getChannel() == channelid){
                    return eachmessage.getId();
                }
            }
        }
        return null;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        JavaApplication app = new JavaApplication();

        // service 구현 객체 생성
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        while(true){
            System.out.println("======== Discord Service ========");
            System.out.println("1. User Create");
            System.out.println("2. User Search");
            System.out.println("3. User Delete");
            System.out.println("4. Channel Create");
            System.out.println("5. Channel Search");
            System.out.println("6. Channel Delete");
            System.out.println("7. Message Create");
            System.out.println("8. Message Search");
            System.out.println("9. Message Delete");
            System.out.println("0. Exit");
            System.out.print("input number: ");

            int num = scanner.nextInt();
            // 개행문자 처리
            scanner.nextLine();

            switch (num){
                case 1:
                    System.out.print("User name: ");
                    String name = scanner.nextLine();
                    UUID userid = app.checkUser(userService, name);
                    if(userid == null) {
                        userService.createUser(name);
                        System.out.println("User Create Complete");
                    }
                    else
                        System.out.println("User Already exist");
                    break;
                case 2:
                    System.out.print("User name for search: ");
                    String name2 = scanner.nextLine();
                    UUID userid2 = app.checkUser(userService, name2);
                    if(userid2 != null)
                        System.out.println("User name: " + name2 + ", exist");
                    else
                        System.out.println("Can't find user");
                    break;
                case 3:
                    System.out.print("User name for delete: ");
                    String name3 = scanner.nextLine();
                    UUID userid3 = app.checkUser(userService, name3);
                    if(userid3 != null){
                        userService.deleteUser(userid3);
                        System.out.println("User name: " + name3 + ", delete complete");
                    }
                    else
                        System.out.println("Can't find user");
                    break;
                // ----------------- Channel --------------------
                case 4:
                    System.out.print("Channel name: ");
                    String name1 = scanner.nextLine();
                    System.out.print("Channel topic: ");
                    String topic = scanner.nextLine();
                    UUID channelID = app.checkChannel(channelService, name1);
                    if(channelID == null){
                        channelService.createChannel(name1, topic);
                        System.out.println("Channel Create complete");
                    }
                    else
                        System.out.println("Channel is Already exist");
                    break;
                case 5:
                    System.out.print("Channel name for search: ");
                    String name4 = scanner.nextLine();
                    UUID channelID2 = app.checkChannel(channelService, name4);
                    if(channelID2 != null)
                        System.out.println("Channel name: " + name4 + ", exist");
                    else
                        System.out.println("Can't find channel");
                    break;
                case 6:
                    System.out.print("Channel name for delete: ");
                    String name5 = scanner.nextLine();
                    UUID channelID3 = app.checkChannel(channelService, name5);
                    if(channelID3 != null) {
                        System.out.println("Channel name: " + name5 + ", delete complete");
                        channelService.deleteChannel(channelID3);
                    }
                    else
                        System.out.println("Can't find channel");
                    break;
                // ------------------- Message ----------------------
                case 7:
                    System.out.print("User name: ");
                    String username7 = scanner.nextLine();
                    System.out.print("Channel name: ");
                    String channelname7 = scanner.nextLine();
                    System.out.print("Message: ");
                    String message = scanner.nextLine();
                    UUID userID4 = app.checkUser(userService, username7);
                    UUID channelID4 = app.checkChannel(channelService, channelname7);
                    UUID messageID = app.checkMessage(messageService, userID4, channelID4);
                    if(messageID == null) {
                        if(userID4 != null){
                            if(channelID4 != null){
                                messageService.createMessage(message, userID4, channelID4);
                                System.out.println("Message Create Complete");
                            }
                            // channel이 존재하지 않을 경우
                            else
                                System.out.println("Can't find channel");
                        }
                        // user가 존재하지 않을 경우
                        else
                            System.out.println("Can't find User");
                    }
                    // message가 이미 존재할 경우
                    else
                        System.out.println("Message already exist");
                    break;
                case 8:
                    System.out.print("User name for search: ");
                    String username = scanner.nextLine();
                    System.out.print("Channel name for search: ");
                    String channelname = scanner.nextLine();
                    UUID userID5 = app.checkUser(userService, username);
                    UUID channelID5 = app.checkChannel(channelService, channelname);
                    UUID messageID2 = app.checkMessage(messageService, userID5, channelID5);
                    if(messageID2 != null) {
                        if(userID5 != null){
                            if(channelID5 != null)
                                System.out.println("Message: " + messageService.getOneMessage(messageID2));
                            // channel이 존재하지 않을 경우
                            else
                                System.out.println("Can't find channel");
                        }
                        // user가 존재하지 않을 경우
                        else
                            System.out.println("Can't find User");
                    }
                    // message가 이미 존재할 경우
                    else
                        System.out.println("Can't find message");
                    break;
                case 9:
                    System.out.print("User name for delete: ");
                    String username9 = scanner.nextLine();
                    System.out.print("Channel name for delete: ");
                    String channelname9 = scanner.nextLine();
                    UUID userID6 = app.checkUser(userService, username9);
                    UUID channelID6 = app.checkChannel(channelService, channelname9);
                    UUID messageID3 = app.checkMessage(messageService, userID6, channelID6);
                    if(messageID3 != null) {
                        if(userID6 != null){
                            if(channelID6 != null) {
                                System.out.println("Message: " + messageService.getOneMessage(messageID3) + ", delete complete");
                                messageService.deleteMessage(messageID3);
                            }
                            // channel이 존재하지 않을 경우
                            else
                                System.out.println("Can't find channel");
                        }
                        // user가 존재하지 않을 경우
                        else
                            System.out.println("Can't find User");
                    }
                    // message가 존재하지 않을 경우
                    else
                        System.out.println("Can't find message");
                    break;
                case 0:
                    System.out.println("Program Exit");
                    scanner.close();
                    return;
                default:
                    System.out.println("0~9 number only");
            }
        }
    }
}
