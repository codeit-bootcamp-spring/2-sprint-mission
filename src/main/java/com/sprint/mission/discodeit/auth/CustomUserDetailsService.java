package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final JpaUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByName(username)
        .map(user -> org.springframework.security.core.userdetails.User.builder()
            .username(user.getName())
            .password(user.getPassword())
            .build())
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username));
  }
}