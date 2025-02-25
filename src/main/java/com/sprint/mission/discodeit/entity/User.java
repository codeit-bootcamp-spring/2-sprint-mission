package com.sprint.mission.discodeit.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class User extends BaseEntity {
    private String password;
    private UserRepository userRepository;

    public User(String name, String password) {
        super(name);
        this.password = password;
        userRepository = new UserRepository();
    }

    public void add(Server server) {
        severList.add(server);
        System.out.println("서버 리스트 추가 성공");
    }

    public void remove(Server server) {
        severList.remove(server);
        System.out.println("서버 삭제 성공");
    }

    public void print() {
//        severList.stream().forEach(System.out::println);
        for (Server server : severList) {
            System.out.println(server.getName());
        }
    }

    public void search() {

    }

    public void update() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("바꾸실 서버의 이름을 입력하시오. : ");
        String s = sc.nextLine();
        for (Server server : severList) {
            if (server.getName() == s) {
                System.out.printf("\n바꿀 이름을 입력하시오. : ");
                String b = sc.nextLine();
                server.setName(s);
                return;
            }
        }
        System.out.println("\n해당 서버는 존재하지 않습니다.");
    }

}
