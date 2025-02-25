package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Repository.UserRepository;
import com.sprint.mission.discodeit.service.RepositoryService;

import java.util.List;
import java.util.Scanner;

public class JCFUserRepository implements RepositoryService<User, Server> {
    private static JCFUserRepository instance;

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
        List<Server> severList = userRepository.getSeverList();
        return severList;
    }

    @Override
    public void add(User user, Server server) {
        List<Server> severList = repository(user);
        severList.add(server);
        System.out.println("서버 추가 성공");
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
        sc.close();
        for (Server server : list) {
            if (server.getName() == s) {
                list.remove(server);
                System.out.println("\n서버 삭제 성공");
                return;
            }
        }
        System.out.println("삭제할 서버의 이름이 존재하지 않습니다.");
    }

    @Override
    public List<Server> print(User user) {
        List<Server> list = repository(user);
        if (list.isEmpty()) {
            return null;
        }

        for (Server server : list) {
            System.out.println(server.getName());
        }

        return list;
    }

    @Override
    public void update(User user) {
        List<Server> severList = print(user);

        Scanner sc = new Scanner(System.in);
        System.out.print("업데이트할 서버의 이름을 입력하시오. : ");
        String s = sc.nextLine();
        for (Server server : severList) {
            if (server.getName() == s) {
                System.out.print("\n바꿀 이름을 입력하시오. : ");
                String b = sc.nextLine();
                server.setName(b);
                System.out.println("서버 업데이트 성공");
                return;
            }
        }
        sc.close();
        System.out.println("해당 서버의 이름이 존재하지 않습니다.");
    }

    @Override
    public void search(User user) {

    }
}
