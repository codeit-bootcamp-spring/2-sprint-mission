package com.sprint.mission.discodeit.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.stereotype.Component;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.util.List;

@Component
public class FilterLogger implements CommandLineRunner {

  private final FilterChainProxy filterChainProxy;

  public FilterLogger(FilterChainProxy filterChainProxy) {
    this.filterChainProxy = filterChainProxy;
  }

  @Override
  public void run(String... args) {
    System.out.println("===== Security Filter 목록 =====");

    List<SecurityFilterChain> chains = filterChainProxy.getFilterChains();

    for (SecurityFilterChain chain : chains) {
      // DefaultSecurityFilterChain으로 캐스팅
      if (chain instanceof org.springframework.security.web.DefaultSecurityFilterChain defaultChain) {
        RequestMatcher matcher = defaultChain.getRequestMatcher();
        System.out.println("RequestMatcher: " + matcher);

        defaultChain.getFilters().forEach(filter ->
            System.out.println(" - " + filter.getClass().getSimpleName())
        );
      }
    }
  }
}
