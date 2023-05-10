package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

// JpaRepository<>를 [공통 인터페이스]라고 부른다.
public interface TeamRepository extends JpaRepository<Team,Long> {
}
