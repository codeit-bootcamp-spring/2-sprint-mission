package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    @Override
    public void save(String nickname, String password) {
        User user = new User(nickname, password);
        try {
            String fileName = "user.ser";
            // 파일 존재 여부 확인
            boolean append = new File(fileName).exists();

            FileOutputStream fos = new FileOutputStream(fileName, true);
            ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
            oos.writeObject(user);

            oos.close();
            fos.close();

            System.out.println("[회원가입 성공]" + user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByUser(UUID userUUID) {
        try (FileInputStream fis = new FileInputStream("user.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    if (user.getId().equals(userUUID)) return user;
                } catch (EOFException e) {
                    // 파일의 끝 도달 시 브레이크
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[실패] 회원아이디가 존재하지 않습니다.");
        return null;
    }

    @Override
    public List<User> findAllUser() {
        List<User> userList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("user.ser");
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    userList.add(user);
                } catch (EOFException e) {
                    // 파일의 끝 도달 시 브레이크
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void update(UUID userUUID, String nickname) {
        List<User> userList = findAllUser();

        userList.stream()
                .filter(user -> user.getId().equals(userUUID))
                .findAny()
                .ifPresentOrElse(
                    user -> {
                        user.updateNickname(nickname);
                        System.out.println("[성공]닉네임 변경 완료" + user);
                    },
                    () -> System.out.println("[실패]수정하려는 아이디가 존재하지 않습니다"));

        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (User user : userList) {
                oos.writeObject(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        List<User> users = findAllUser();

        boolean removed = users.removeIf(user -> user.getId().equals(uuid));

        if (!removed) {
            System.out.println("[실패]존재하지 않는 사용자");
            return;
        } else {
            System.out.println("[성공]사용자 삭제 완료");
        }

        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (User user : users) {
                oos.writeObject(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UUID login(UUID userUUID, String password) {
        User userInfo = findByUser(userUUID);
        if (userInfo == null) {
            return null;
        }

        if (!userInfo.getPassword().equals(password)) {
            System.out.println("[실패]비밀번호가 일치하지 않습니다.");
            return null;
        }

        System.out.println("[성공] 로그인 완료");
        return userInfo.getId();
    }
}
