package com.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssertJCheckTest {

  @Test
  void assertj_is_working() {
    String name = "허재석";
    assertThat(name).contains("허");
  }
}