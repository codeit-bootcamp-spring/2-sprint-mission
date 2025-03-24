package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "content");

    private final List<BinaryContent> binaryContentsData;

    public FileBinaryContentRepository() {
        binaryContentsData = new ArrayList<>();
        init();
    }


    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentsData.add(binaryContent);
        Path path = DIRECTORY.resolve(binaryContent.getId() + ".jpg");
        saveToFile(path, binaryContent);
        return binaryContent;
    }

    private void init() {
        if (!Files.exists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveToFile(Path path, BinaryContent binaryContent) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<BinaryContent> load() {
        try (Stream<Path> path = Files.list(DIRECTORY)) {
            return path
                    .map(this::loadFromFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BinaryContent loadFromFile(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (BinaryContent) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void remove(BinaryContent binaryContent) {
        try {
            if (binaryContent != null && Files.exists(DIRECTORY.resolve(binaryContent.getId() + ".jpg"))) {
                Files.delete(DIRECTORY.resolve(binaryContent.getId() + ".jpg"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
