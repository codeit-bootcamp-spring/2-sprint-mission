package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.composit.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ServerService;

import java.util.Collection;
import java.util.Scanner;

public class JCFServerService implements ServerService {
//    Server server;
//
//    @Override
//    public void initServer() {
//        Scanner sc = new Scanner(System.in);
//        System.out.printf("서버명을 입력하시오.\n서버명: ");
//        String name = sc.nextLine();
//
//        this.server = new Server(name);
//        sc.close();
//    }
//
//    @Override
//    public void addChannel(Channel channel) {
//        server.channels.put(channel.getId(), channel);
//        server.update();
//        System.out.println("채널이 추가되었습니다.");
//    }
//
//    @Override
//    public Channel searchChannel() {
//        Scanner sc = new Scanner(System.in);
//
//        System.out.printf("찾으실 id를 입력하시오.\nid명: ");
//        String id = sc.nextLine();
//        Channel channel = server.channels.get(id);
//        if (channel == null) {
//            System.out.println("해당 채널이 없습니다.");
//            sc.close();
//            return null;
//        } else {
//            System.out.println("조회 성공");
//            sc.close();
//            return channel;
//        }
//    }
//
//    @Override
//    public void updateChannel(Channel channel) {
//        server.channels.put(channel.getId(), channel);
//        server.update();
//        System.out.println("채널이 수정되었습니다.");
//    }
//
//    @Override
//    public void removeChannel() {
//        //채널은 반드시 1개 이상은 있어야 함
//        if (server.channels.size() == 1) {
//            System.out.println("최소한 1개 이상의 채널이 존재하여야 합니다.");
//            return;
//        } else {
//            Scanner sc = new Scanner(System.in);
//
//            System.out.printf("삭제할 id를 입력하시오.\nid명: ");
//            String id = sc.nextLine();
//            Channel remove = server.channels.remove(id);
//
//            if (remove == null) {
//                System.out.println("삭제할 채널이 없습니다.");
//                sc.close();
//                return;
//            } else {
//                server.update();
//                sc.close();
//                System.out.println("삭제 성공");
//            }
//        }
//    }
//
//    @Override
//    public void printAllChannels() {
//        Collection<Channel> values = server.channels.values();
//        for (Channel channel : values){
//            System.out.println("--------------------------------------------");
//            System.out.println(channel);
//            System.out.println("--------------------------------------------");
//        }
//    }
//
//    @Override
//    public void printAllUsers() {
//        Collection<User> values = server.users.values();
//        for (User user : values){
//            System.out.println("--------------------------------------------");
//            System.out.println(user);
//            System.out.println("--------------------------------------------");
//        }
//
//    }
//
//    @Override
//    public void addUser(User user) {
//        server.users.put(user.getId(), user);
//        server.update();
//        System.out.println("사용자가 서버에 추가되었습니다.");
//    }
//
//    @Override
//    public User searchUser() {
//        Scanner sc = new Scanner(System.in);
//
//        System.out.printf("찾으실 id를 입력하시오.\nid명: ");
//        String id = sc.nextLine();
//        User user = server.users.get(id);
//        if (user == null) {
//            System.out.println("해당 사용자가 없습니다.");
//            sc.close();
//            return null;
//        } else {
//            System.out.println("조회 성공");
//            sc.close();
//            return user;
//        }
//    }
//
//    @Override
//    public void updateUser(User user) {
//        server.users.put(user.getId(), user);
//        server.update();
//        System.out.println("사용자가 수정되었습니다.");
//    }
//
//    @Override
//    public void removeUser() {
//        //사용자는 반드시 1명 이상은 있어야 함
//        if (server.channels.size() == 1) {
//            System.out.println("최소한 1명 이상의 유저가 존재하여야 합니다.");
//            return;
//        } else {
//            Scanner sc = new Scanner(System.in);
//
//            System.out.printf("삭제할 id를 입력하시오.\nid명: ");
//            String id = sc.nextLine();
//            User remove = server.users.remove(id);
//
//            if (remove == null) {
//                System.out.println("삭제할 사용자가 없습니다.");
//                sc.close();
//                return;
//            } else {
//                server.update();
//                sc.close();
//                System.out.println("삭제 성공");
//            }
//        }
//    }
//

}
