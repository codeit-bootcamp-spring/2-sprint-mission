package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.Repository.RepositoryService;

import java.util.List;
import java.util.Scanner;

public class JCFContainerRepository implements RepositoryService<Server, Container> {
    private static JCFContainerRepository instance;
    private Container head;

    private JCFContainerRepository() {
    }

    public static JCFContainerRepository getInstance() {
        if (instance == null) {
            instance = new JCFContainerRepository();
        }
        return instance;
    }

    @Override
    public List<Container> repository(Server server) {
        ServerRepository serverRepository = server.getServerRepository();
        return serverRepository.getContainers();
    }

    @Override
    public Container getHead() {
        return head;
    }

    @Override
    public void add(Server server, Container container) {
        List<Container> list = repository(server);
        list.add(container);
        head = container;
        System.out.println(container.getName() + "컨테이너 저장 성공");
    }

    @Override
    public void remove(Server server) {
        List<Container> list = print(server);
        if (list == null) {
            System.out.println("아무것도 저장되어있지 않습니다.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 컨테이너의 이름을 입력하시오. : ");
        String s = sc.nextLine();
        for (Container data : list) {
            if (data.getName().equals(s)) {
                list.remove(data);
                System.out.println(data.getName() + "컨테이너 삭제 성공");
                return;
            }
        }
        System.out.println("해당 컨테이너가 존재하지 않습니다.");
    }

    @Override
    public List<Container> print(Server server) {
        List<Container> list = repository(server);
        if (list.isEmpty()) {
            return null;
        }
        System.out.println("==============" + server.getName() + "서버==================");
        for (Container data : list) {
            System.out.println(data.getName());
        }
        System.out.println("==============================================");

        return list;
    }

    @Override
    public void update(Server server) {
        List<Container> list = print(server);

        Scanner sc = new Scanner(System.in);
        System.out.print("업데이트할 컨테이너의 이름을 입력하시오. : ");
        String s = sc.nextLine();
        for (Container data : list) {
            if (data.getName().equals(s)) {
                System.out.print("바꿀 이름을 입력하시오. : ");
                s = sc.nextLine();

                System.out.println(data.getName() + "컨테이너 업데이트 성공");
                return;
            }
        }
//        sc.close();
        System.out.println("해당 컨테이너가 존재하지 않습니다.");
    }

    @Override
    public void search(Server server) {

    }
}
