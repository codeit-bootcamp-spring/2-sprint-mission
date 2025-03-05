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
        Server target = serverList.stream().filter(server -> server.getName().equals(name))
                .findFirst().orElse(null);

        System.out.println("get 시점: " + target.getId());

        return target;
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
            System.out.println(i + 1 + " : " + list.get(i).getName());
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

        return removeServer(list, targetName, directory);
    }

    @Override
    public boolean removeServer(UUID userId, String targetName) {
        UserRepository JCFUserRepository = getUserRepository(userId);
        List<Server> list = JCFUserRepository.getServerList();

        if (list == null) {
            System.out.println("서버 삭제 실패 : list null값");
            return false;
        }
        return removeServer(list, targetName, directory);

    }

    private boolean removeServer(List<Server> list, String targetName, Path directory) {
        Server targetServer = list.stream().filter(server -> server.getName().equals(targetName))
                .findFirst().orElse(null);
        if (targetServer != null) {
            Path targetPath = directory.resolve(targetServer.getId().toString().concat(".ser"));
            try {
                if (Files.deleteIfExists(targetPath)) {
                    //로그 찍기
                    System.out.println("파일 삭제 성공");
                    return true;
                } else {
                    System.out.println("파일이 존재하지 않습니다.");
                }
            } catch (IOException e) {
                System.out.println("파일 삭제 중 오류 발생");
            }
        } else {
            System.out.println("삭제할 서버가 존재하지 않습니다.");
        }
        return false;
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
            Path oldFilePath = directory.resolve(targetServer.getId().toString().concat(".ser"));
            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                System.err.println("기존 서버 파일 삭제 실패: " + e.getMessage());
            }

            targetServer.setName(replaceName);
            Path filsPath = directory.resolve(targetServer.getId().toString().concat(".ser"));

            try (FileOutputStream fos = new FileOutputStream(filsPath.toFile());
                 ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                oos.writeObject(targetServer);
            } catch (IOException e) {
                //로그 찍기
                System.out.println("User Repository 저장 실패");
                throw new RuntimeException(e);
            }
            return true;
        } else {
            System.out.println("업데이트할 서버가 존재하지 않습니다.");
        }
        return false;
    }


    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                System.out.println("디렉토리 저장 실패");
                throw new RuntimeException(e);
            }
        }
    }

}
