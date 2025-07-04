package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // ⭐️ 이 @Service 어노테이션이 아주 중요해! Spring이 이 클래스를 Bean으로 등록하게 해줘.
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // DB에서 username으로 사용자 조회
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

    // Spring Security가 사용하는 UserDetails 객체로 변환해서 반환
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(), // DB에 저장된 암호화된 비밀번호
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())) // 사용자의 권한
    );
  }
}