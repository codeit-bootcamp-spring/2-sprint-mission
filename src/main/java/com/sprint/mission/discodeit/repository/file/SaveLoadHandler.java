package com.sprint.mission.discodeit.repository.file;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveLoadHandler<T> {
    private final String FILE;

    public SaveLoadHandler(String FILE) {
        this.FILE = FILE;
    }


    public Map<UUID, T> loadData(){
        File file = new File(FILE);
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

    public synchronized void saveData(Map<UUID, T> data){
        try(FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(data);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
