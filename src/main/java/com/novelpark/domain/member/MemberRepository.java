package com.novelpark.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByLoginId(String loginId);

  boolean existsByLoginId(String loginId);

  @Query("SELECT m.loginId FROM Member m WHERE m.email = :email AND m.name = :name")
  Optional<String> findLoginIdByEmailAndName(
      @Param("email") String email,
      @Param("name") String name
  );
}
