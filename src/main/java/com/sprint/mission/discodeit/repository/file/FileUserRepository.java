package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private final static File FILE = new File("user.ser");

    @Override
    public User userSave(String nickname, String password) {
        User user = new User(nickname, password);
        try {

            boolean append = FILE.exists();

            FileOutputStream fos = new FileOutputStream(FILE, true);
            ObjectOutputStream oos = append ? new AppendObjectOutputStream(fos) : new ObjectOutputStream(fos);
            oos.writeObject(user);

            oos.close();
            fos.close();

            return user;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<User> findUserById(UUID userUUID) {
        if (!FILE.exists()) {
            return Optional.empty();
        }

        try (FileInputStream fis = new FileInputStream(FILE);
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    if (user.getId().equals(userUUID)) return Optional.of(user);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllUser() {
        List<User> userList = new ArrayList<>();
        if (!FILE.exists()) {
            return userList;
        }

        try (FileInputStream fis = new FileInputStream(FILE);
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    userList.add(user);
                } catch (EOFException e) {
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
    public User updateUserNickname(UUID userUUID, String nickname) {
        List<User> userList = findAllUser();

        userList.stream()
                .filter(user -> user.getId().equals(userUUID))
                .findAny()
                .map(user -> {
                    user.updateNickname(nickname);
                    return user;
                })
                .orElse(null);
        try (FileOutputStream fos = new FileOutputStream(FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (User user : userList) {
                oos.writeObject(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteUserById(UUID userUUID) {
        List<User> userList = findAllUser();
        boolean removed  = userList.removeIf(user -> user.getId().equals(userUUID));
        if (!removed) return false;
        try (FileOutputStream fos = new FileOutputStream(FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (User user : userList) {
                oos.writeObject(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
