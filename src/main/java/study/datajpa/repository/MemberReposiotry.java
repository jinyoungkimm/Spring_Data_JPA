package study.datajpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

// Member [엔티티]에 대한 [Spring Data JPA] 등록!!!
public interface MemberReposiotry extends JpaRepository<Member,Long> {

    //Spring Data JPA가 메서드 명을 보고 아래 [쿼리 메서드]([조회] 메서드)를 자동으로 구현을 해준다.
    //-> findByusernameAndAgeGreaterThan : findById + username + And + Age + Greater Than + (username,age)
    // [username]과 [age Greater Than]은 SELECT문의 WHERE절에 Select Condition으로 들어 가며
    // 매개변수 (username)과 (age)는 바인딩돼서 SQL문이 만들어 진다.
    List<Member> findByusernameAndAgeGreaterThan(String username,int age);

   // @Query(name = "Member.findByUsername") // Spring Data JPA가 JPQL문을 [자동]으로 작성을 해준다.
    List<Member> findByUsername(@Param("username") String usernmae); // 메서드 명은 아무거나로 지어도 됨.

    // JPQL을 Member 클래스의 @NamedQuery가 아닌 [쿼리] 메서드(Repository 계층)위에 바로 적어 놓을 수가 있다.
    // -> [정적]인 JPQL이므로, 애플리케이션 로딩 시점(컴파일 타임)에 쿼리문 에러 체크를 할 수가 있고,
    // Member 클래스와 MemberRepsitory 인터페이스를 왔다 갔다가 하면서 개발하지 않아도 된다.
    @Query("SELECT m FROM Member m WHERE m.username = :username AND m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
