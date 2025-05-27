package com.sprint.mission.discodeit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DiscodeitApplicationTests {


  @Autowired
  private Environment env;

  @Test
  void contextLoads() {
    System.out.println("▶ contextLoads started");
    System.out.println("▶ accessKey = " + env.getProperty("discodeit.storage.s3.access-key"));
    System.out.println("▶ storage type = " + env.getProperty("discodeit.storage.type"));
  }
}
