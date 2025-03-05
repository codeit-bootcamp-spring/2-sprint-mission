package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    public FileUserRepository() {
        loadFromFile("Users.csv");
    }

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
        saveInFile(users, "Users.csv");
    }

    @Override
    public void update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            saveInFile(users, "Users.csv");
        }
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
        saveInFile(users, "Users.csv");
    }

    public static void saveInFile(Map<UUID, User> users, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // 헤더
            bw.write("key,value");
            bw.newLine();

            for (Map.Entry<UUID, User> entry : users.entrySet()) {
                UUID key = entry.getKey();
                User value = entry.getValue();
                bw.write(key + "," + value);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<UUID, User> loadFromFile(String filename){
        Map<UUID, User> loadedMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false; // 첫 줄(헤더) 건너뛰기
                    continue;
                }
                String[] tokens = line.split(",");
                UUID key = UUID.fromString(tokens[0]);
                User value = new User(tokens[1]);
                loadedMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedMap;
    }
}
