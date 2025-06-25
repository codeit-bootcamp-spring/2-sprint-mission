package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(CustomUserDetails::from)
        .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없음: " + username));
  }
}
