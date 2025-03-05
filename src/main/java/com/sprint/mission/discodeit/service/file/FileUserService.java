package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.file.FileUserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {
    private static FileUserService instance;
    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    Path directory = Paths.get(System.getProperty("user.dir"), "data","serverList.ser");

    private FileUserService() {

    }

    public static FileUserService getInstance() {
        if (instance == null) {
            instance = new FileUserService();
        }
        return instance;
    }

    //레포지토리 생성
    private UserRepository getUserRepository(UUID id) {
        UserRepository userRepository = userTable.get(id);
        if (userRepository == null) {

            UserRepository repository = new FileUserRepository();

            userTable.put(id, repository);

            userRepository = repository;
        }
        return userRepository;
    }

    @Override
    public Server createServer(String name) {
        return CreateServerFactory.getInstance().create(name);
    }

    @Override
    public void addServer(UUID userId, String name) {
        UserRepository userRepository = getUserRepository(userId);
        Server server = createServer(name);
        userRepository.save(server);

        //로그
        System.out.println("저장 시점 name :"+server.getId());
    }

    @Override
    public void addServer(UUID userId, Server server) {
        UserRepository userRepository = getUserRepository(userId);
        userRepository.save(server);

        //로그
        System.out.println("저장 시점 server:"+server.getId());
    }

    @Override
    public Server getServer(UUID userId, String name) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> serverList = userRepository.getServerList();
        for (Server server : serverList) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    @Override
    public void printServer(UUID userId) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getServerList();
        printServer(list);
    }

    @Override
    public void printServer(List<Server> list) {
        System.out.println("\n=========서버 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " id : " + list.get(i).getId());
            System.out.println(i + 1 + " name : " + list.get(i).getName());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeServer(UUID userId) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getServerList();
        if (list == null) {
            System.out.println("서버 삭제 실패 : list null값");
            return false;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 서버 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return removeServer(list, targetName, userRepository);
    }

    @Override
    public boolean removeServer(UUID userId, String targetName) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getServerList();

        if (list == null) {
            System.out.println("서버 삭제 실패 : list null값");
            return false;
        }
        return removeServer(list, targetName,userRepository);

    }

    private boolean removeServer(List<Server> list, String targetName, UserRepository userRepository) {
        Server targetServer = list.stream().filter(server -> server.getName().equals(targetName))
                .findFirst().orElse(null);
        if (targetServer == null) {
            System.out.println("삭제할 서버가 존재하지 않습니다.");
            return false;
        } else {
            //서버 리스트를 가져와서 목표한 서버를 삭제한 뒤
            list.remove(targetServer);
            //서버를 다시 저장한다.
            userRepository.updateServerList(list);
        }
        return true;
    }


    @Override
    public boolean updateServer(UUID userId) {
        UserRepository userRepository = getUserRepository(userId);
        List<Server> list = userRepository.getServerList();

        Scanner sc = new Scanner(System.in);
        System.out.print("바꿀려고 하는 서버의 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return updateServer(userId, list, targetName);
    }

    @Override
    public boolean updateServer(UUID userId, String targetName) {
        UserRepository JCFUserRepository = getUserRepository(userId);
        List<Server> list = JCFUserRepository.getServerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("서버 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return updateServer(userId, list, targetName, replaceName);
    }

    @Override
    public boolean updateServer(UUID userId, String targetName, String replaceName) {
        UserRepository JCFUserRepository = getUserRepository(userId);
        List<Server> list = JCFUserRepository.getServerList();

        return updateServer(userId, list, targetName, replaceName);
    }

    private boolean updateServer(UUID userId, List<Server> list, String targetName) {
        Scanner sc = new Scanner(System.in);
        System.out.print("서버 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();
        return updateServer(userId, list, targetName, replaceName);
    }

    private boolean updateServer(UUID userId, List<Server> list, String targetName, String replaceName) {
        UserRepository userRepository = getUserRepository(userId);
        Server targetServer = list.stream().filter(server -> server.getName().equals(targetName))
                .findFirst().orElse(null);
        if (targetServer != null) {
            for (Server server : list) {
                if (server.getId().equals(targetServer.getId())) {
                    server.setName(replaceName);
                    userRepository.updateServerList(list);
                    return true;
                }
            }
        } else {
            System.out.println("업데이트할 서버가 존재하지 않습니다.");
        }
        return false;
    }
}
