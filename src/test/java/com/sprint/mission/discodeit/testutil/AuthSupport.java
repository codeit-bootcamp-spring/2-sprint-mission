package com.sprint.mission.discodeit.testutil;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthSupport {

  private AuthSupport() {
  }

  public static void setTestAuthentication(User user, Role role) {
    UserResult userResult = new UserResult(user.getId(), null, null, "", "", null, role, false);
    CustomUserDetails principal = new CustomUserDetails(userResult, "");
    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

    Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

}
