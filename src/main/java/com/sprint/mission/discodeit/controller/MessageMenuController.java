package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.menus.MessageMenu;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class MessageMenuController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChannelService channelService;
    private final Scanner scanner;
    private static User loggedUser;
    private static Channel currentChannel;


    public MessageMenuController(UserService userService, ChannelService channelService, MessageService messageService, Scanner scanner) {
        this.messageService = messageService;
        this.userService =  userService;
        this.channelService = channelService;
        this.scanner = scanner;

    }

    public void handleMessageMenu() {
        if(!logInUser(getIdFromInput("로그인할 유저 ID 입력= "))){
            return;
        }
        if(!currentChannel(getIdFromInput("접속할 채널 ID 입력= "))){
            return;
        }
        boolean run = true;
        while (run) {
            for (MessageMenu option : MessageMenu.values()) {
                System.out.println(option.getCode() + ". " + option.getDescription());
            }
            System.out.print("선택: ");

            String choice = scanner.nextLine();
            MessageMenu selectedMenu = MessageMenu.getByCode(choice);

            if (selectedMenu == null) {
                System.out.println("잘못된 입력입니다. ");
                continue;
            }

            try {
                run = execute(selectedMenu);
            }catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean execute(MessageMenu selectedMenu) {
        switch (selectedMenu) {
            case CREATE:
                createMessage();
                return true;
            case FIND:
                findMessage();
                return true;
            case FINDALL:
                findAllMessage();
                return true;
            case UPDATE:
                updateMessage();
                return true;
            case DELETE:
                deleteMessage();
                return true;
            case CHANGEDCHANNEL:
                changeChannel();
                return true;
            case BACK:
                return false;
            default:
                System.out.println("잘못된 입력입니다.");
                return true;
        }
    }

    private UUID getIdFromInput(String description){
        try {
            System.out.print(description);
            return UUID.fromString(scanner.nextLine());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private String getMessageInput(String description){
        try {
            System.out.print(description);
            return scanner.nextLine();
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private boolean logInUser(UUID loggedInUserId) {
        try {
            if(loggedUser == userService.find(loggedInUserId)) {
                System.out.println("이미 로그인 되어있는 유저입니다.");
                return false;
            }
            loggedUser = userService.find(loggedInUserId);
            return true;
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean currentChannel(UUID loggedInUserId) {
        try {
            if(currentChannel == channelService.find(loggedInUserId)){
                System.out.println("이미 같은 채널입니다.");
                return false;
            }
            currentChannel = channelService.find(loggedInUserId);
            return true;

        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    private void createMessage(){
        String message = getMessageInput("작성할 메세지: ");
        messageService.create(message,loggedUser.getId(),currentChannel.getId());
    }

    private void findMessage(){
        System.out.println("조회할 필터를 선택해주세요.");
        for(MessageFindMenu m : MessageFindMenu.values()){
            System.out.println(m.getCode() + ". " + m.getDescription());
        }
        System.out.print("선택: ");
        String findMenuChoice = scanner.nextLine();
        MessageFindMenu findMenu = MessageFindMenu.getByCode(findMenuChoice);

        if (findMenu == null) {
            System.out.println("잘못된 입력입니다. ");
            return;
        }
        switch (findMenu) {
            case USER:
                System.out.println(messageService.findByUser(loggedUser.getId()));
                return;
            case CHANNEL:
                System.out.println(messageService.findByChannel(currentChannel.getId()));
                return;
            case USER_AND_CHANNEL:
                System.out.println(messageService.findByUserAndByChannel(loggedUser.getId(), currentChannel.getId()));
                return;
            default:
                System.out.println("잘못된 입력입니다.");
        }
    }

    private void findAllMessage(){
        System.out.println(messageService.findAll());
    }

    private void updateMessage(){
        UUID messageId = getIdFromInput("수정할 메세지의 ID를 입력해주세요: ");
        String newMessage = getMessageInput("새로운 메세지: ");
        messageService.update(messageId,newMessage);

    }

    private void deleteMessage(){
        UUID id = getIdFromInput("삭제할 메세지ID를 입력하세요: ");
        messageService.delete(id);
    }

    private void changeChannel(){
        currentChannel(getIdFromInput("옮길 채널ID를 입력해주세요: "));
    }


    private enum MessageFindMenu{
        USER("1", "유저"),
        CHANNEL("2", "채널"),
        USER_AND_CHANNEL("3", "전부");

        final String code;
        final String description;

        MessageFindMenu(String code, String description){
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static MessageFindMenu getByCode(String code){
            for (MessageFindMenu menu : MessageFindMenu.values()) {
                if (menu.code.equals(code)) {
                    return menu;
                }
            }
            return null;
        }
    }
}
