package com.sprint.mission.discodeit.security.util;

import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

public final class SecurityUtil {

  private SecurityUtil() {
  }

  public static UUID getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
      throw new IllegalStateException("인증된 사용자가 없습니다.");
    }

    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    return principal.getUserResult().id();
  }

  public static boolean hasRole(String roleName) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(authority -> authority.equals("ROLE_" + roleName));
  }

}