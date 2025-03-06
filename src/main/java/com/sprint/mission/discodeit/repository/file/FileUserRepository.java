package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    @Override
    public User userSave(String nickname, String password) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Optional<User> findUserById(UUID userUUID) {
        return Optional.empty();
    }

    @Override
    public List<User> findAllUser() {
        return List.of();
    }

    @Override
    public User updateUserNickname(UUID userUUID, String nickname) {
        return null;
    }

    @Override
    public boolean deleteUserById(UUID userUUID) {
        return false;
    }
}
