package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    // 사용자명으로 사용자 정보 조회 (로그인 시 사용)
    Optional<User> findByUsername(String username);

    // 사용자명 존재 여부 확인 (회원가입/수정 시 중복 체크)
    boolean existsByUsername(String username);

    // 이메일 존재 여부 확인 (회원가입/수정 시 중복 체크)
    boolean existsByEmail(String email);
}
