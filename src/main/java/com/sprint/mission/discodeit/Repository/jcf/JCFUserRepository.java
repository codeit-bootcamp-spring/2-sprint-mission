package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.RepositoryService;

import java.util.List;
import java.util.Scanner;

public class JCFUserRepository implements RepositoryService<User, Server> {
    private static JCFUserRepository instance;
    private Server head;

    private JCFUserRepository() {
    }

    public static JCFUserRepository getInstance() {
        if (instance == null) {
            instance = new JCFUserRepository();
        }
        return instance;
    }

    @Override
    public List<Server> repository(User user) {
        UserRepository userRepository = user.getUserRepository();
        return userRepository.getSeverList();
    }

    @Override
    public Server getHead() {
        return head;
    }

    @Override
    public void add(User user, Server server) {
        List<Server> severList = repository(user);
        severList.add(server);
        head = server;
        System.out.println(server.getName() + "서버 추가 성공");
    }

    @Override
    public void remove(User user) {
        List<Server> list = print(user);
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 서버의 이름을 입력하시오. : ");
        String s = sc.nextLine();
        for (Server data : list) {
            if (data.getName().equals(s)) {
                list.remove(data);
                System.out.println(data.getName() + "서버 삭제 성공");
                return;
            }
        }
        System.out.println("해당 서버가 존재하지 않습니다.");
    }

    @Override
    public List<Server> print(User user) {
        List<Server> list = repository(user);
        if (list.isEmpty()) {
            return null;
        }
        System.out.println("=================" + user.getName() + "유저==================");

        for (Server data : list) {
            System.out.println(user.getName() + " : " + data.getName());
        }
        System.out.println("================================================");

        return list;
    }

    @Override
    public void update(User user) {
        List<Server> severList = print(user);

        Scanner sc = new Scanner(System.in);
        System.out.print("업데이트할 서버의 이름을 입력하시오. : ");
        String s = sc.next();
        for (Server data : severList) {
            if (data.getName().equals(s)) {
                System.out.print("바꿀 이름을 입력하시오. : ");
                s = sc.next();
                data.setName(s);
                System.out.println(data.getName() + "서버 업데이트 성공");
                return;
            }
        }
//        sc.close();
        System.out.println("해당 서버가 존재하지 않습니다.");
    }

    @Override
    public void search(User user) {

    }
}
