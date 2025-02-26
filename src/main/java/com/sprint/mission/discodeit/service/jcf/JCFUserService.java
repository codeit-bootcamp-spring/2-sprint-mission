package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Factory.CreateChannalFactory;
import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Factory.Factory;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListUserRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
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
 * @JongwonLee
 */
public class JCFUserService implements UserService {
    private static JCFUserService instance;

    private JCFUserService() {
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    //레포지토리 생성
    private UserRepository getUserRepository(UUID id) {
        UserRepository userRepository = userTable.get(id);
        if (userRepository == null) {
            userTable.put(id, new LinkedListUserRepository());
        }
        return userRepository;
    }

    public Server createServer(String name) {
        return CreateServerFactory.getInstance().create(name);
    }

    public Channel createChannel(String name) {
        return CreateChannalFactory.getInstance().create(name);
    }

    @Override
    public void addServer(UUID userId, String name) {
        UserRepository userRepository = getUserRepository(userId);
        Server server = createServer(name);
        userRepository.add(server);

        //로그
        System.out.println(server.getName() + " 서버 추가 성공");
    }

    @Override
    public void addServer(UUID userId, Server server) {
        UserRepository userRepository = getUserRepository(userId);
        userRepository.add(server);

        //로그
        System.out.println(server.getName() + " 서버 추가 성공");
    }

    @Override
    public Server getServer(UUID userId, String name) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getList();
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
    public void printServer(UUID userId) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getList();
        System.out.println("=========서버 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================");
    }

    @Override
    public void printServer(List<Server> list) {
        System.out.println("=========서버 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================");
    }

    @Override
    public boolean removeServer(UUID userId, String targetName) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getList();
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
    public boolean updateServer(UUID userId, String targetName, String replaceName) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getList();
        for (Server server : list) {
            if (server.getName().equals(targetName)) {
                server.setName(replaceName);
                //로그
                System.out.println(server.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }

    @Override
    public void addChannel(UUID userId, Channel Channel) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getList();
        printServer(list);
        Scanner sc = new Scanner(System.in);
        System.out.print("추가할 서버의 번호를 입력하시오. : ");
        int i = sc.nextInt();
        sc.nextLine();
        Server server = list.get(i);

    }

    @Override
    public void printChannel(UUID userId) {

    }

    @Override
    public boolean removeChannel(String targetName) {
        return false;
    }

    @Override
    public boolean updateChannel(String targetName, String replaceName) {
        return false;
    }
}
