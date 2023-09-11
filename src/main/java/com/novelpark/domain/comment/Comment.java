package com.novelpark.domain.comment;

import com.novelpark.domain.AuditingFields;
import com.novelpark.domain.member.Member;
import com.novelpark.domain.novel.Novel;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "comment")
@Entity
public class Comment extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(nullable = false, length = 5000)
  private String content;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Long recommend;

  @JoinColumn(name = "novel_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Novel novel;

  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Builder
  public Comment(Long seq, String content, Novel novel, Member member) {
    this.seq = seq;
    this.content = content;
    this.novel = novel;
    this.member = member;
  }

  public static Comment from(String content, Long novelSeq, Long memberSeq) {
    return Comment.builder()
        .content(content)
        .member(Member.builder().seq(memberSeq).build())
        .novel(Novel.builder().seq(novelSeq).build())
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Comment)) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(seq, comment.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }
}
