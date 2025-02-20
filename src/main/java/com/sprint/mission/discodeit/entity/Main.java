package com.sprint.mission.discodeit.entity;

public class Main {
    public static void main(String[] args) {
        User user1 = new User("김현호");
        System.out.println(user1); // 이렇게 하면, toString() 자동 호출됌! 기본 제공 기능인 듯

        Channel channel1 = new Channel("백엔드 톡방");
        System.out.println(channel1);

        Message message1 = new Message("백엔드 자바 스프링 톡방 입니다~!");
        System.out.println(message1);

        System.out.println("-----");

        user1.setUserName("데이빗");
        System.out.println(user1); // 업데이트 시간도 같이 수정 함.

    }
}
