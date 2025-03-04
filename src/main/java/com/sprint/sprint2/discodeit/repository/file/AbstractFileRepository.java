package com.sprint.sprint2.discodeit.repository.file;

import java.io.*;

public abstract class AbstractFileRepository<T> {
    private final String filePath;

    protected AbstractFileRepository(String filePath) {
        this.filePath = filePath;
    }

    public void save(T entity) {
        try (FileOutputStream fos = new FileOutputStream(filePath, true);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(entity);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
