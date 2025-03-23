package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.channelService.ChannelCreateDTO;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.menus.ChannelMenu;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class ChannelMenuController {
    private final ChannelService channelService;
    private final Scanner scanner;

    public ChannelMenuController(ChannelService channelService, Scanner scanner) {
        this.channelService = channelService;
        this.scanner = scanner;
    }

    public void handleChannelMenu() {
        boolean run = true;
        while (run) {
            for (ChannelMenu option : ChannelMenu.values()) {
                System.out.println(option.getCode() + ". " + option.getDescription());
            }
            System.out.print("선택: ");


            String choice = scanner.nextLine();
            ChannelMenu selectedMenu = ChannelMenu.getByCode(choice);

            if (selectedMenu == null) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            try {
                run = execute(selectedMenu);
            }
            catch (IllegalArgumentException | NoSuchElementException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private boolean execute(ChannelMenu selectedMenu) {
        switch (selectedMenu) {
            case CREATE:
                createChannel();
                return true;
            case FIND:
                findChannel();
                return true;
            case FINDALL:
                findAllChannel();
                return true;
            case UPDATE:
                updateChannel();
                return true;
            case DELETE:
                deleteChannel();
                return true;
            case BACK:
                return false;
            default:
                System.out.println("잘못된 입력입니다.");
                return true;
        }
    }

    private UUID getChannelIdFromInput(String description){
        try {
            System.out.print(description);
            return UUID.fromString(scanner.nextLine());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private String getChannelNameFromInput(String description){
        try {
            System.out.print(description);
            return scanner.nextLine();
        }catch (IllegalArgumentException e){
           throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private ChannelType getChannelTypeFromInput(String description){
        for(ChannelType type : ChannelType.values()){
            System.out.print(type.toString() + " ");
        }
        System.out.print(description);
        try {
            return ChannelType.valueOf(scanner.nextLine().toUpperCase());
        }catch (IllegalArgumentException e){
            System.out.println("값이 제대로 입력되지 않아 PUBLIC으로 설정되었습니다.");
        }
        return ChannelType.PUBLIC;
    }

    private void createChannel() {
        System.out.println("생성할 채널타입, 채널명을 입력해주세요: ");
        ChannelType type = getChannelTypeFromInput("채널타입: ");
        String channelName = getChannelNameFromInput("채널명: ");
        ChannelCreateDTO channelCreateDto = new ChannelCreateDTO(type, channelName);
        System.out.println("채널 생성 완료: \n" + channelService.create(channelCreateDto));

    }

    private void findChannel() {
        UUID id = getChannelIdFromInput("조회할 채널 ID를 입력해주세요: ");
        System.out.println("조회된 채널: " + channelService.find(id));
    }

    private void findAllChannel() {
        System.out.println(channelService.findAll());
    }

    private void updateChannel() {
        UUID id = getChannelIdFromInput("업데이트할 채널 ID를 입력해주세요: ");
        System.out.print("변경할 채널 타입, 채널명을 입력해주세요: ");
        ChannelType type =  getChannelTypeFromInput("채널 타입: ");
        String channelName = getChannelNameFromInput("채널명: ");

        System.out.println("업데이트 완료: \n" + channelService.update(id, channelName, type));
    }

    private void deleteChannel() {
        UUID id = getChannelIdFromInput("삭제할 채널 ID를 입력하세요: ");
        channelService.delete(id);
    }
}

