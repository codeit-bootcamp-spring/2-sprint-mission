package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.TextMessage;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Scanner;

public class JCFMessageService implements MessageService {
    @Override
    public Message write(Channel channel) {
        Scanner sc = new Scanner(System.in);

        System.out.printf("채널명을 입력하시오.\n채널명: ");
        String txt = sc.nextLine();
        Message message = new TextMessage(txt);


        return null;
    }
}
