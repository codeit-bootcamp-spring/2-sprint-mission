package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryTypeTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userRepository_구현체_확인() {
        System.out.println("주입된 UserRepository 구현체: " + userRepository.getClass().getSimpleName());

        if (userRepository instanceof FileUserRepository fileRepo) {
            System.out.println("FileUserRepository가 주입되었습니다.");
            assertTrue(true);
        } else if (userRepository instanceof JCFUserRepository jcfRepo) {
            System.out.println("JCFUserRepository가 주입되었습니다.");
            assertTrue(true);
        } else {
            fail("예상하지 못한 구현체가 주입됨: " + userRepository.getClass());
        }
    }
}
