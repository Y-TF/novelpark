package com.novelpark.infrastructure.hash;

public interface PasswordEncoder {

  String encrypt(String password);

  boolean matches(String enteredPassword, String encryptedPassword);
}
