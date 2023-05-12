package study.datajpa.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    List<Member> findListByUsername(String username);

    // 2] 반환 타입 : 단건
    Member findMemberByUsername(String username);

    //3] 반환 타입 : 단건 with Optional
    Optional<Member> findOptionalByUsername(String username);


    //이제는 Spring Data JPA로 [페이징]과 [정렬]을 구현해볼거다.
    // 1] Page 인터페이스 : 쿼리용 SQL문 1번, totalCount 쿼리용 SQL문 1번 : 총 2번의 쿼리 발생(실행해서 SQL문 확인!게시물에서 참조)
    Page<Member> findPageByAge(int age, Pageable pageable); // 쿼리 메서드의 이름으로 Spring Data JPA가 JPQL를 만들어 준다.

    // 2] Slice 인터페이스
     Slice<Member> findSliceByAge(int age, Pageable pageable);

     //  totalCount 계산은 부하가 크다. 왜냐하면, DB Table 전~체를 다 조회해야 하기 때문인데, 상황에 따라서 totalCount 계산을 최적화 해줘야 한다.
     // 우리가 쿼리 문을 날리게 되면, 대게의 경우, JOIN연산이 일어난다.
     // 여기서는 Member -> Team( 다 : 1 )과 left Outer Join으로 예시를 들겠다.
     // 만약, 2개의 Table 사이에 left outer join이 일어나게 된다면, 그 결과의 table의 totalcount나 member table의 total count나 값이 같다.
     // -> 이러한 점들을 이용해서 Spring Data JPA는 @Query( countQuery = " select ~~~ " ) 식으로 totalcount용 쿼리를 따로 짤 수 있게 해놓음
     @Query(value = "SELECT m FROM Member m LEFT JOIN m.team t",  // 원래는 여기에서 totalCount 쿼리 계산도 같이 일어남!
             countQuery = "SELECT COUNT(m.username) FROM Member m") // 이 부분에서 left outer Join으로 totalCount를 계산하지 않고,
                                                                    // Member Table 하나만을 가지고, totalCount를 계산한다. 결과는 똑같이 나온다.
                                                                    // 실행해서 sql문 확인해봐라(countQuery가 있고, 없고의 SQL문 차이 확인)
       Page<Member> findtotalCountByAge(int age, Pageable pageable);
}
