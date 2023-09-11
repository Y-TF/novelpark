package com.novelpark.presentation;

import com.novelpark.application.comment.CommentService;
import com.novelpark.presentation.dto.request.comment.CommentRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{novelSeq}/comments")
  public ResponseEntity<Void> register(@RequestBody CommentRegisterRequest request,
      @PathVariable Long novelSeq,
      @SessionAttribute("memberSeq") Long memberSeq) {
    commentService.register(request.getContent(), memberSeq, novelSeq);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}