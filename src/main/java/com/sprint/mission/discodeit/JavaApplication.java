package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

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
                    userService.CreateUser(name);
                    System.out.println("User Create Complete");
                    break;
                case 2:
                    System.out.print("User name for search: ");
                    String name2 = scanner.nextLine();
                    Map<UUID, User> temp = userService.getAllUser();
                    boolean exist = false;
                    for(User eachuser : temp.values()) {
                        if (eachuser.getUsername().equals(name2)) {
                            System.out.println("User name: " + name2 + ", exist");
                            exist = true;
                            break;
                        }
                    }
                    if(!exist)
                        System.out.println("Can't find user");
                    break;
                case 3:
                    System.out.print("User name for delete: ");
                    String name3 = scanner.nextLine();
                    Map<UUID, User> temp3 = userService.getAllUser();
                    boolean exist3 = false;
                    for(User eachuser : temp3.values()) {
                        if (eachuser.getUsername().equals(name3)) {
                            userService.DeleteUser(eachuser.getId());
                            System.out.println("User name: " + name3 + ", delete complete");
                            exist3 = true;
                            break;
                        }
                    }
                    if(!exist3)
                        System.out.println("Can't find user");
                    break;
                // ----------------- Channel --------------------
                case 4:
                    System.out.print("channel name: ");
                    String name1 = scanner.nextLine();
                    System.out.print("channel topic: ");
                    String topic = scanner.nextLine();
                    channelService.CreateChannel(name1, topic);
                    System.out.println("channel complete");
                    break;
                case 5:
                    System.out.print("Channel name for search: ");
                    String name4 = scanner.nextLine();
                    Map<UUID, Channel> temp4 = channelService.getAllChannel();
                    boolean exist4 = false;
                    for(Channel eachchannel : temp4.values()) {
                        if (eachchannel.getName().equals(name4)) {
                            System.out.println("Channel name: " + name4 + ", exist");
                            exist4 = true;
                            break;
                        }
                    }
                    if(!exist4)
                        System.out.println("Can't find channel");
                    break;
                case 6:
                    System.out.print("Channel name for delete: ");
                    String name5 = scanner.nextLine();
                    Map<UUID, Channel> temp6 = channelService.getAllChannel();
                    boolean exist5 = false;
                    for(Channel eachchannel : temp6.values()) {
                        if (eachchannel.getName().equals(name5)) {
                            System.out.println("Channel name: " + name5 + ", delete complete");
                            channelService.DeleteChannel(eachchannel.getId());
                            exist5 = true;
                            break;
                        }
                    }
                    if(!exist5)
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
                    if(messageService.CreateMessage(message, username7, channelname7))
                        System.out.println("Message Create Complete");
                    break;
                case 8:
                    System.out.print("User name for search: ");
                    String username = scanner.nextLine();
                    System.out.print("Channel name for search: ");
                    String channelname = scanner.nextLine();
                    Map<UUID, Message> temp8 = messageService.getAllMessage();
                    Map<UUID, Channel> temp7 = channelService.getAllChannel();
                    UUID messageid = null;
                    for(Message eachmessage : temp8.values()){
                        if(userService.getoneUser(eachmessage.getUser()).get().getUsername().equals(username)){
                            for(Channel eachchannel : temp7.values()){
                                if(channelService.getOneChannel(eachchannel.getId()).get().getName().equals(channelname)){
                                    messageid = eachmessage.getId();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if(messageid == null)
                        System.out.println("Can't find message");
                    else
                        System.out.println("Message: " + temp8.get(messageid).getContent());
                    break;
                case 9:
                    System.out.print("User name for delete: ");
                    String username9 = scanner.nextLine();
                    System.out.print("Channel name for delete: ");
                    String channelname9 = scanner.nextLine();
                    Map<UUID, Message> temp9 = messageService.getAllMessage();
                    Map<UUID, Channel> temp10 = channelService.getAllChannel();
                    UUID messageid9 = null;
                    for(Message eachmessage : temp9.values()){
                        if(userService.getoneUser(eachmessage.getUser()).get().getUsername().equals(username9)){
                            for(Channel eachchannel : temp10.values()){
                                if(channelService.getOneChannel(eachchannel.getId()).get().getName().equals(channelname9)){
                                    messageid9 = eachmessage.getId();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if(messageid9 == null)
                        System.out.println("Can't find message");
                    else
                        System.out.println("Message: " + temp9.get(messageid9).getContent() + ", delete complete");
                    messageService.DeleteMessage(messageid9);
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
