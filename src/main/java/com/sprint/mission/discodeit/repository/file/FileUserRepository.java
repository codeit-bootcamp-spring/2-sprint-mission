package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;

public class FileUserRepository {
    public static void serializeUser(User user, String filepath){
        // 파일에 객체 직렬화
        try(FileOutputStream fos = new FileOutputStream(filepath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);){
            oos.writeObject(user);
            System.out.println("User 객체 직렬화 성공 " + filepath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static User deserializeUser(String filepath){
        try(FileInputStream fis = new FileInputStream(filepath);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            return (User) ois.readObject();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
