package study.datajpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

// Member [엔티티]에 대한 [Spring Data JPA] 등록!!!
public interface MemberReposiotry extends JpaRepository<Member,Long> {

//  JpaRepository<Member,Long> :  JpaRepository<JPA로 등록한 [엔티티],[엔티티]의 id [자료형]>


}
