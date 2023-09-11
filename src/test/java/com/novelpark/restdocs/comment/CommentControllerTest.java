package com.novelpark.restdocs.comment;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelpark.presentation.CommentController;
import com.novelpark.presentation.dto.request.comment.CommentRegisterRequest;
import com.novelpark.restdocs.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

@WebMvcTest(CommentController.class)
public class CommentControllerTest extends ControllerTestSupport {

  @DisplayName("댓글을 등록한다.")
  @Test
  void test() throws Exception {
    // given
    willDoNothing().given(commentService).register(anyString(), anyLong(), anyLong());
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("memberSeq", 1L);

    // when & then
    mockMvc.perform(post("/api/{novelId}/comments", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .session(session)
            .content(objectMapper.writeValueAsString(new CommentRegisterRequest("이 소설 재밌다~"))))
        .andExpect(status().isCreated())
        .andDo(MockMvcRestDocumentation.document("registerComment",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()))
        );
  }
}
