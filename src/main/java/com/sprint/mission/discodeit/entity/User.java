package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Factory.Factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class User extends BaseEntity {
    private Server head = null;
    private String password;
    private List<Server> severList;

    public User(String id, String name, String password) {
        super(id, name);
        this.password = password;
        severList = new LinkedList<>();
    }

    public void currentHead() {
        System.out.println(head.getName());
    }

    public void createServer(String name) {
        Factory serverFactory = CreateServerFactory.getInstance();
        Server server = serverFactory.create(name);
        severList.add(server);
        head = server;
        System.out.println("서버 생성 성공");
    }

    public void createChannel(String name) {
        if (head == null) {
            System.out.println("서버를 하나 이상 만드세요.");
        } else {
            head.addChannel(name);
            System.out.println("채널 생성 성공");
        }
    }

    public void printServer() {
        System.out.println("=================================================");
        for (int i = 0; i < severList.size(); i++) {
            System.out.println(i + 1 + " : " + severList.get(i).getName());
        }
        System.out.println("=================================================\n");
    }

    public void printChannel() {
        head.print();
    }

    public void replaceHead() {
        if (severList.size() < 2) {
            System.out.println("서버는 반드시 1개 이상 필요합니다.");
            return;
        }
        System.out.println("====서버 목록====");
        printServer();
        System.out.print("바라볼 서버의 인덱스를 입력하시오. : ");
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        sc.nextLine();
        head = severList.get(i-1);
        System.out.println(i + " : " + severList.get(i-1).getName());
        sc.close();
    }

    public void removeServer(String targetName) {
        if (severList.size() < 2) {
            System.out.println("서버는 반드시 1개 이상 필요합니다.");
            return;
        }
        for (Server server : severList) {
            if (server.getName() == targetName) {
                severList.remove(server);
                System.out.println("서버 삭제 성공");
                return;
            }
        }
        System.out.println("서버 삭제에 실패했습니다.");
    }

    public void removeChannel(String targetName) {
        head.remove(targetName);
    }

    public void updateServer(String targetName, String replaceName) {
        for (Server server : severList) {
            if (server.getName() == targetName) {
                server.setName(replaceName);
                return;
            }
        }
        System.out.println("업데이트할 서버가 존재하지 않습니다.");
    }

    public void updateChannel(String targetName, String replaceName) {
        head.update(targetName,replaceName);
    }

}
