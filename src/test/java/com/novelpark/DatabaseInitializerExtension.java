package com.novelpark;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseInitializerExtension implements AfterEachCallback {

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    DatabaseInitializer databaseInitializer = (DatabaseInitializer) SpringExtension.getApplicationContext(
            context)
        .getBean("databaseInitializer");
    databaseInitializer.truncateTables();
  }
}
