package com.sprint.mission.discodeit.composit;

import com.sprint.mission.discodeit.Iterator.Iterator;
import com.sprint.mission.discodeit.entity.Message;

import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;


public class Channel extends CategoryAndChannel {
    private LinkedList<Message> messageList;

    public Channel(String id, String name) {
        super(id, name);
        messageList = new LinkedList<>();
    }

    @Override
    public void add(String str) {
        Message message = new Message("M" + messageList.size(), "M" + messageList.size(),str);
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
        print();
        Scanner sc = new Scanner(System.in);
        System.out.println("몇 번째 메시지를 삭제하시겠습니까?");
        int j = sc.nextInt();
        sc.nextLine();
        sc.close();
        messageList.remove(j - 1);
        System.out.println("메시지 삭제 성공");
    }

    public void updateMessage() {
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

    @Override
    public boolean checkCategory(CategoryAndChannel item) {
        if (item.getClass().isInstance(Category.class)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public LinkedList<CategoryAndChannel> getList() {
        return null;
    }

    @Override
    public void add(CategoryAndChannel channel) {

    }

    @Override
    public void remove(CategoryAndChannel channel) {

    }

    @Override
    public void update() {

    }

    @Override
    public void update(CategoryAndChannel channel, String replaceName) {

    }
    @Override
    public void printHead() {

    }

    @Override
    public void printCurrent() {

    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
