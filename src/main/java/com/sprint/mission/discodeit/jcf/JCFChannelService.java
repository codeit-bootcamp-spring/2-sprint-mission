package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class JCFChannelService implements ChannelService {
    JCFMessageService messageinstance = JCFMessageService.getInstance();
    private static JCFChannelService channelinstance;
    private List<Message> messageList;

    private JCFChannelService(){}

    public JCFChannelService getInstance() {
        if (channelinstance == null) {
            channelinstance = new JCFChannelService();
        }
        return channelinstance;
    }

    public void setMessageList(LinkedList<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void add(String str) {
        Message message = messageinstance.add(str);
        messageList.add(message);
    }

    @Override
    public void print() {
        if (messageList.isEmpty()) {
            System.out.println("메시지가 없습니다.");
            return;
        }
        for (int i = 0; i < messageList.size(); i++) {
            System.out.println(i + 1 + " : " + messageList.get(i).getStr());
        }
    }

    @Override
    public void remove() {
        if (messageList.isEmpty()) {
            System.out.println("메시지가 없습니다.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("몇 번째 메시지를 삭제하시겠습니까?");
        int j = sc.nextInt();
        sc.nextLine();
        sc.close();
        messageList.remove(j - 1);
        System.out.println("메시지 삭제 성공");
    }

    @Override
    public void update() {
        if (messageList.isEmpty()) {
            System.out.println("메시지가 없습니다.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("========메시지 목록=========");
        for (int i = 0; i < messageList.size(); i++) {
            System.out.println(i + 1 + " : " + messageList.get(i).getStr());
        }
        System.out.printf("몇 번째 메시지를 수정하시겠습니까? : ");
        int j = sc.nextInt();
        sc.nextLine();
        Message message = messageList.get(j - 1);

        System.out.printf("\n수정할 텍스트를 입력하시오.");
        String s = sc.nextLine();
        message.setStr(s);

        sc.close();
        System.out.println("메시지 수정 성공");
    }
}
