package com.sprint.mission.discodeit.repository.file;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
public class SaveLoadHandler<T> {

    public Map<UUID, T> loadData(String fileName) {
        File file = new File(fileName);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)){
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void saveData(String fileName, Map<UUID, T> data){
        try(FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(data);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
