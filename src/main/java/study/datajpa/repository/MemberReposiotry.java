package study.datajpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

// Member [엔티티]에 대한 [Spring Data JPA] 등록!!!
public interface MemberReposiotry extends JpaRepository<Member,Long> {

    //Spring Data JPA가 메서드 명을 보고 아래 [쿼리 메서드]([조회] 메서드)를 자동으로 구현을 해준다.
    //-> findByusernameAndAgeGreaterThan : findById + username + And + Age + Greater Than + (username,age)
    // [username]과 [age Greater Than]은 SELECT문의 WHERE절에 Select Condition으로 들어 가며
    // 매개변수 (username)과 (age)는 바인딩돼서 SQL문이 만들어 진다.
    List<Member> findByusernameAndAgeGreaterThan(String username,int age);

}
