package com.sprint.sprint2.discodeit.repository.file;

import com.sprint.sprint2.discodeit.entity.User;
import com.sprint.sprint2.discodeit.repository.UserRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FIleUserRepository extends AbstractFileRepository<User> implements UserRepository {

    private static final String FILE_PATH = "users.ser";

    public FIleUserRepository() {
        super(FILE_PATH);
    }
}
