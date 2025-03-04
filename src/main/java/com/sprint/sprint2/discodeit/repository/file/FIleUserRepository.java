package com.sprint.sprint2.discodeit.repository.file;

import com.sprint.sprint2.discodeit.entity.User;
import com.sprint.sprint2.discodeit.repository.UserRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class FIleUserRepository implements UserRepository {

    private static final String FILE_PATH = "users.ser";

    @Override
    public void save(User user) {
        boolean append = new File(FILE_PATH).exists();
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH, true);
             ObjectOutputStream oos = getObjectOutputStream(fos, append)) {
            oos.writeObject(user); // 객체 직렬화하여 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ObjectOutputStream을 반환하는 메서드
    private ObjectOutputStream getObjectOutputStream(FileOutputStream fos, boolean append) throws IOException {
        if (append) {
            return new AppendableObjectOutputStream(fos); // 기존 파일에 추가하는 경우 (헤더 없음)
        } else {
            return new ObjectOutputStream(fos); // 새로운 파일 생성 시 (헤더 포함)
        }
    }

    // 기존 ObjectOutputStream의 헤더 중복 문제를 해결하는 클래스
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        protected AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // 헤더를 다시 쓰지 않도록 수정
        }
    }



}
