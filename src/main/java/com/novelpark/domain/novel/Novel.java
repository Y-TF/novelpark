package com.novelpark.domain.novel;

import com.novelpark.domain.AuditingFields;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "novel")
@Entity
public class Novel extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(length = 100, nullable = false)
  private String title;

  @Column(length = 512, nullable = false)
  private String coverImageUrl;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer views;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer recommend;

  @Builder
  public Novel(Long seq, String title, String coverImageUrl) {
    this.seq = seq;
    this.title = title;
    this.coverImageUrl = coverImageUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Novel)) {
      return false;
    }
    Novel novel = (Novel) o;
    return Objects.equals(seq, novel.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }
}
