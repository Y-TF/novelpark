package com.novelpark.application.comment;

import com.novelpark.domain.comment.Comment;
import com.novelpark.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

  private final CommentRepository commentRepository;

  @Transactional
  public void register(String content, Long memberSeq, Long novelSeq) {
    commentRepository.save(Comment.from(content, memberSeq, novelSeq));
  }
}
