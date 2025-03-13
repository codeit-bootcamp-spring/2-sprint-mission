package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JCFUserService implements UserService {
    private static volatile JCFUserService instance;
    private final Map<UUID, UserRepository> userTable = new HashMap<>();
    private final UserRepository userRepository;

    public JCFUserService( ) {
        System.out.println("JCF User Service 가동");
        userRepository = new JCFUserRepository();
    }


    //레포지토리 생성
//    private UserRepository getUserRepository(UUID id) {
//        UserRepository userRepository = userTable.get(id);
//        if (userRepository == null) {
//            //주입 시점
//            userTable.put(id, repository);
//            userRepository = repository;
//        }
//        return userRepository;
//    }


    public UUID createServer(String name) {
        Server server = new Server(name);
        userRepository.save(server);
        return server.getId();
    }

    @Override
    public boolean joinServer(UUID userId, String name) {


    }


    @Override
    public Server getServer(UUID userId, String name) {
        List<Server> serverList = userRepository.getServerList();
        return serverList.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void printServer(UUID userId) {
        List<Server> list = userRepository.getServerList();
        System.out.println("\n=========서버 목록==========");
        list.forEach(s -> System.out.println(s.getId() + " : " + s.getName()));
        System.out.println("=========================\n");

    }

    public boolean removeServer(UUID userId) {
        List<Server> list = userRepository.getServerList();
        if (list == null) {
            System.out.println("서버 삭제 실패 : list null값");
            return false;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 서버 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return removeServer(list, targetName);
    }

    @Override
    public boolean removeServer(UUID userId, String targetName) {
        List<Server> list = userRepository.getServerList();
        if (list == null) {
            System.out.println("서버 삭제 실패 : list null값");
            return false;
        }
        return removeServer(list, targetName);
    }

    private boolean removeServer(List<Server> list, String targetName) {
        Server targetServer = list.stream().filter(s -> s.getName().equals(targetName)).findFirst().orElse(null);
        if (targetServer == null) {
            System.out.println("삭제할 서버가 존재하지 않습니다.");
            return false;
        }
        list.remove(targetServer);

        //로그
        System.out.println(targetServer.getName() + " 이(가) 삭제됩니다.");

        return true;
    }

    public boolean updateServer(UUID userId) {
        List<Server> list = userRepository.getServerList();

        Scanner sc = new Scanner(System.in);
        System.out.print("바꿀려고 하는 서버의 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return updateServer(list, targetName);
    }


    public boolean updateServer(UUID userId, String targetName) {
        List<Server> list = userRepository.getServerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("서버 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return updateServer(list, targetName, replaceName);
    }

    public boolean updateServer(UUID userId, String targetName, String replaceName) {
        List<Server> list = userRepository.getServerList();
        return updateServer(list, targetName, replaceName);
    }


    private boolean updateServer(List<Server> list, String targetName) {
        Scanner sc = new Scanner(System.in);
        System.out.print("서버 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();
        return updateServer(list, targetName, replaceName);
    }

    private boolean updateServer(List<Server> list, String targetName, String replaceName) {
        Server targetServer = list.stream().filter(s -> s.getName().equals(targetName))
                .findFirst().orElse(null);
        if (targetServer != null) {
            targetServer.setName(replaceName);
            //로그
            System.out.println(targetName + " 이름이 " + targetServer.getName() + " 이(가) 됩니다.");
            return true;
        }
        System.out.println("업데이트할 서버가 존재하지 않습니다.");
        return false;
    }
}
