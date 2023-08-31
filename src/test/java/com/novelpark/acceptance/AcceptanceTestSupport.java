package com.novelpark.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.SupportRepository;
import com.novelpark.application.image.S3Uploader;
import com.novelpark.application.mail.MailService;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@AcceptanceTest
public abstract class AcceptanceTestSupport {

  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  protected SupportRepository supportRepository;

  @MockBean
  protected S3Uploader s3Uploader;
  @MockBean
  protected MailService mailService;

  protected File createStubFile() throws IOException {
    return File.createTempFile("test", ".png");
  }
}
