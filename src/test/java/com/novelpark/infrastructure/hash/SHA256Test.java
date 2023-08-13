package com.novelpark.infrastructure.hash;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SHA256Test {

  private final SHA256 sha256 = new SHA256();

  @DisplayName("문자열을 암호화하는데 성공한다.")
  @Test
  void encryptTest() {
    String encrypted = sha256.encrypt("1");
    assertThat(encrypted).isNotNull();
  }

  @DisplayName("같은 문자열에 대해서는 암호화한 값이 동일하다.")
  @Test
  void isSameEncryptedText() {
    String encrypted = sha256.encrypt("abcd");
    assertThat(encrypted).isEqualTo(sha256.encrypt("abcd"));
  }
}
