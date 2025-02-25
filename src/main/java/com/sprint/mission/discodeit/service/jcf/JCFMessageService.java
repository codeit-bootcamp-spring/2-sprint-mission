package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.TextMessage;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Scanner;

public class JCFMessageService implements MessageService {
    @Override
    public Message write() {
        Scanner sc = new Scanner(System.in);

        System.out.println("메시지를 입력하시오: ");
        String txt = sc.nextLine();
        Message message = new TextMessage(txt);

        return message;
    }

    @Override
    public Message randomWrite() {
        Message message = new TextMessage("A");
        return message;
    }
}
