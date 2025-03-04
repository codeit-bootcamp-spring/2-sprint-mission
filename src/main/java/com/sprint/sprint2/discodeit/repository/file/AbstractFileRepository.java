package com.sprint.sprint2.discodeit.repository.file;

import static java.lang.System.in;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractFileRepository<T> {
    private final String filePath;

    protected AbstractFileRepository(String filePath) {
        this.filePath = filePath;
    }

    protected void writeToFile(Map<UUID, T> entity) {
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
