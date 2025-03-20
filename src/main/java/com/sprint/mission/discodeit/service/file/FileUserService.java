package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.SaveUserParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserParamDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserService implements UserService {

    @Override
    public void save(SaveUserParamDto saveUserParamDto) {

        User user = new User(
                saveUserParamDto.username(), saveUserParamDto.password(),
                saveUserParamDto.nickname(), saveUserParamDto.email(),
                saveUserParamDto.profileUUID());
        try {
            String fileName = "user.ser";
            // 파일 존재 여부 확인
            boolean append = new File(fileName).exists();

            FileOutputStream fos = new FileOutputStream(fileName, true);
            ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
            oos.writeObject(user);

            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FindUserDto findByUser(UUID userUUID) {
        //try (FileInputStream fis = new FileInputStream("user.ser");
        //     ObjectInputStream ois = new ObjectInputStream(fis);
        //) {
        //    while (true) {
        //        try {
        //            User user = (User) ois.readObject();
        //            if (user.getId().equals(userUUID)) return user;
        //        } catch (EOFException e) {
        //            // 파일의 끝 도달 시 브레이크
        //            break;
        //        }
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //} catch (ClassNotFoundException e) {
        //    throw new RuntimeException(e);
        //}
        //System.out.println("[실패] 회원아이디가 존재하지 않습니다.");
        return null;
    }

    @Override
    public List<FindUserDto> findAllUser() {
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
        return userList.stream()
                .map(user -> findByUser(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(UpdateUserParamDto updateUserDto) {
        List<User> userList = Collections.EMPTY_LIST;

        userList.stream()
                .filter(user -> user.getId().equals(updateUserDto.userUUID()))
                .findAny()
                .ifPresentOrElse(
                        user -> {
                            user.updateNickname(updateUserDto.nickname());
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
        List<User> users = Collections.EMPTY_LIST;

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
}
