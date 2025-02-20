package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.TextMessage;
import com.sprint.mission.discodeit.entity.VoiceMessage;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Scanner;

public class JCFChannelService implements ChannelService {
    @Override
    public Channel register() {
        Scanner sc = new Scanner(System.in);
        Channel channel;


        System.out.printf("채널명을 입력하시오.\n채널명: ");
        String name = sc.nextLine();

        Message message;
        while (true) {
            System.out.printf("\n채널 타입을 선택하시오.\n1.텍스트 채널 2.음성 채널: ");
            int i = sc.nextInt();
            sc.nextLine();

            if (i == 1) {
                message = new TextMessage();
                break;
            } else if (i == 2) {
                message = new VoiceMessage();
                break;
            } else {
                System.out.printf("\n잘못된 값을 입력하셨습니다. 다시 입력해주세요.");
            }
            System.out.println();
        }


        channel = new Channel(name, message);
        channel.update();
        System.out.println("채널 생성 성공");
        sc.close();
        return channel;
    }

    @Override
    public Channel update(Channel channel) {
        Scanner sc = new Scanner(System.in);
        String name = channel.getName();
        Message message = channel.getDefaultMessage();;

        System.out.printf("바꾸실 내용이 무엇인가요?\n1.채널명 2.채널 타입");
        int i = sc.nextInt();
        sc.nextLine();
        if (i == 1) {
            System.out.printf("채널명을 입력하시오.\n채널명: ");
            name = sc.nextLine();

        } else if (i == 2) {
            while (true) {
                System.out.printf("\n채널 타입을 선택하시오.\n1.텍스트 채널 2.음성 채널: ");
                int j = sc.nextInt();
                sc.nextLine();

                if (j == 1) {
                    message = new TextMessage();
                    break;
                } else if (j == 2) {
                    message = new VoiceMessage();
                    break;
                } else {
                    System.out.printf("\n잘못된 값을 입력하셨습니다. 다시 입력해주세요.");
                }
                System.out.println();
            }
        } else {
            System.out.printf("\n잘못된 값을 입력하셨습니다.");
        }

        channel.setName(name);
        channel.setDefaultMessage(message);
        channel.update();

        System.out.println("채널 수정 성공");
        sc.close();
        return channel;

    }
}
