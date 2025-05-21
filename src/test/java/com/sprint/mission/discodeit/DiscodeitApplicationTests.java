package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.storage.s3.S3Properties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(S3Properties.class)
class DiscodeitApplicationTests {

  @Test
  void contextLoads() {
  }

}
