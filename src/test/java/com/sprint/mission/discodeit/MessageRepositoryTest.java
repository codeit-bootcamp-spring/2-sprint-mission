package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class MessageRepositoryTest {

  @Autowired
  private JpaMessageRepository messageRepository;

  @Autowired
  private TestEntityManager entityManager;


}
