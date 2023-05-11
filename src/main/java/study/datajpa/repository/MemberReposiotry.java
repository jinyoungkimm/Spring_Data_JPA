package study.datajpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

// Member [엔티티]에 대한 [Spring Data JPA] 등록!!!
public interface MemberReposiotry extends JpaRepository<Member,Long> {


    List<Member> findByusernameAndAgeGreaterThan(String username,int age);

   // @Query(name = "Member.findByUsername") // Spring Data JPA가 JPQL문을 [자동]으로 작성을 해준다.
    List<Member> findByUsername(@Param("username") String usernmae); // 메서드 명은 아무거나로 지어도 됨.

    @Query("SELECT m FROM Member m WHERE m.username = :username AND m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("SELECT m.username FROM Member m")
    List<String> findUsernameList();


    //@Query안에서 DTO로 조회!
    @Query("SELECT new study.datajpa.dto.MemberDto(m.id,m.username,t.name) FROM Member m JOIN m.team t")
    List<MemberDto> findMemberDto();


    //컬력션 조회(IN 쿼리 이용) : [1:다] 관계에서 생기는 [데이터 중복] 문제가 해결됨.
    @Query("SELECT m FROM Member m WHERE m.username IN :names")
    List<Member> findByNames(@Param("names") List<String> names);

    //Spring Data JPA는 다양한 반환값 타입을 제공한다.
    //1] 반환 타입 : 컬렉션
    List<Member> findListByUsernameS(String username);

    // 2] 반환 타입 : 단건
    Member findMemberByUsername(String username);

    //3] 반환 타입 : 단건 with Optional
    Optional<Member> findOptionalByUsername(String username);



}
