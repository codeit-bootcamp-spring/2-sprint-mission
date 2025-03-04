package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.jcf.impl.LinkedListJCFUserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

/**
 * <h3>유저 서비스 구현체 </h3><p>
 * 서버와 채널을 생성, 삭제, 추가, 업데이트 하는 기능을 수행한다. <br>
 * 유저마다 동일한 기능을 수행해야 한다. <br>
 * 유저마다 유저 레포지토리는 달라야한다.<br>
 * 이를 위해서 Map<유저 ID,유저 레포지토리>를 생성한다.<br>
 * </p>
 *
 * @version 1
 */
public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    private JCFUserService() {
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }


    //레포지토리 생성
    private UserRepository getUserRepository(UUID id) {
        UserRepository userRepository = userTable.get(id);
        if (userRepository == null) {
            UserRepository repository = new LinkedListJCFUserRepository();
            userTable.put(id, repository);
            userRepository = repository;
        }
        return userRepository;
    }


    public Server createServer(String name) {
        return CreateServerFactory.getInstance().create(name);
    }

    @Override
    public void addServer(UUID id, String name) {
        UserRepository JCFUserRepository = getUserRepository(id);
        Server server = createServer(name);
        JCFUserRepository.save(server);

        //로그
        System.out.println(server.getName() + " 서버 추가 성공");
    }

    @Override
    public void addServer(UUID id, Server server) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> serverList = JCFUserRepository.getServerList();
        serverList.add(server);

        //로그
        System.out.println(server.getName() + " 서버 추가 성공");
    }

    @Override
    public Server getServer(UUID id, String name) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        for (Server server : list) {
            if (server.getName().equals(name)) {
                //로그
                System.out.println(server.getName() + " 이(가) 반환됩니다.");
                return server;
            }
        }
        //로그
        System.out.println("존재하지 않습니다.");
        return null;
    }

    @Override
    public void printServer(UUID id) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        printServer(list);
    }

    @Override
    public void printServer(List<Server> list) {
        System.out.println("\n=========서버 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeServer(UUID id) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 서버 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return removeServer(list, targetName);
    }

    @Override
    public boolean removeServer(UUID id, String targetName) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        return removeServer(list, targetName);
    }

    @Override
    public boolean removeServer(List<Server> list, String targetName) {
        for (Server server : list) {
            if (server.getName().equals(targetName)) {
                //로그
                System.out.println(server.getName() + " 이(가) 삭제됩니다.");
                list.remove(server);
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }

    @Override
    public boolean updateServer(UUID id) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("바꿀려고 하는 서버의 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return updateServer(list, targetName);
    }


    @Override
    public boolean updateServer(UUID id, String targetName) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("서버 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return updateServer(list, targetName, replaceName);
    }

    @Override
    public boolean updateServer(UUID id, String targetName, String replaceName) {
        UserRepository JCFUserRepository = getUserRepository(id);
        List<Server> list = JCFUserRepository.getServerList();
        return updateServer(list, targetName, replaceName);
    }

    @Override
    public boolean updateServer(List<Server> list, String targetName) {
        Scanner sc = new Scanner(System.in);
        System.out.print("서버 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();
        return updateServer(list, targetName, replaceName);
    }

    @Override
    public boolean updateServer(List<Server> list, String targetName, String replaceName) {
        for (Server server : list) {
            if (server.getName().equals(targetName)) {
                server.setName(replaceName);
                //로그
                System.out.println(targetName + " 이름이 " + server.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
