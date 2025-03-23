package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Primary
@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final String FILE_PATH = "binary_contents.ser";

    @Override
    public void save(BinaryContent binaryContent) {
        List<BinaryContent> contents = readFromFile();
        contents.removeIf(c -> c.getId().equals(binaryContent.getId()));
        contents.add(binaryContent);
        writeToFile(contents);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return readFromFile().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAll() {
        return readFromFile();
    }

    @Override
    public void delete(UUID id) {
        List<BinaryContent> contents = readFromFile();
        if (contents.removeIf(c -> c.getId().equals(id))) {
            writeToFile(contents);
        }
    }

    private void writeToFile(List<BinaryContent> contents) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<BinaryContent> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}