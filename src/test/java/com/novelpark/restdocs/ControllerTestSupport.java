package com.novelpark.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelpark.application.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected CommentService commentService;
}
