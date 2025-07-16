package com.sprint.mission.discodeit.security.jwt.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtServiceTest extends IntegrationTestSupport {

  @DisplayName("UserDTO 정보로, JWT 토큰을 생성합니다. ")
  @Test
  void createTest(){
      // given

      // when

      // then

  }

  @DisplayName("토큰을 열어보면 사용자 정보, 엑세스 토큰, 리프레시 토큰이 있습니다.")
  @Test
  void test_(){
      // given

      // when

      // then

  }

  @DisplayName("토큰의 유효성을 검사한 수 있습니다.")
  @Test
  void validateTokenTest(){
      // given

      // when

      // then

  }

  @DisplayName("리프레시 토큰을 활용해 엑세스 토큰을 재발급할 수 있습니다.")
  @Test
  void reMakeAccessTokenTest(){
    // given

    // when

    // then

  }

  @DisplayName("리프레시 토큰을 무효화할 수 있습니다.")
  @Test
  void InvalidateRefreshTokenTest(){
      // given

      // when

      // then

  }

  @DisplayName("페이로드에는 다음의 정보를 포함합니다.")
  @Test
  void testPayload(){
    // given

    // when

    // then

  }

}