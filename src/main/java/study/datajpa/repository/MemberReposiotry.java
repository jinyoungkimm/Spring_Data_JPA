package study.datajpa.repository;


import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;


// MemberRepositoryCustom이라는 [사용자 정의 인터페이스]를 상속받게 하면,
// Spring Data JPA가 [사용자 정의 인터페이스] 메서드를 클라이언트에서 호출하면, 제대로 동작하게 알아서 해준다.
// (사용자 정의 인터페이스는 JAVA가 실행시켜 주는 것이 아닌, Spring Data JPA가 동작을 시켜주는 것이다.)
public interface MemberReposiotry extends JpaRepository<Member,Long>, MemberRepositoryCustom {


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


     //이제는 순수 JPA가 아닌, Spring Data JPA 방식으로 벌크 연산을 구현!
     @Modifying(clearAutomatically = true) // Spring Data JPA는 이게 없으면, getResultList()나 getResultSingle()을 호출해 버림
     // @Modifying이 있어야, executeUpdate()가 호출됨.
     @Query("UPDATE Member m set m.age = m.age + 1 WHERE m.age >= :age")
     public int bulkAgePlus(@Param("age") int age);


     //Spring Data JPA로 Fetch Join 구현 1
     @Query("select m from Member m left join fetch m.team")
     List<Member> findMemberFetchJoin();
     //이 방법만으로도 충분히 fetch join을 간편하게 구현이 가능하나,
     //Spring Data JPA는 [@EntityGraph]를 이용하여, 기존과 같이 쿼리 메서드 명으로 fetch join을 구현하는 기능을 지원한다.

     //Spring Data JPA로 Fetch Join 구현 2
     @EntityGraph(attributePaths = {"team"} ) // Member -> Team ( 다 : 1 && 단방향 ) 관계이며, Member가 연관 관계의 주인이다.
     @Override // JpaRepository 공통 인터페이스에는 이미 findAll()이 있으므로, 쿼리 메서드 기능을 이용하기 위해서는 오버라이딩을 해야 한다.
     List<Member> findAll(); // Member를 조회할 때, (N+1) 문제를  @EntityGraph(attributePaths = {"team"} )이 해결!!!
     //attributePaths = {"team"}는 Member를 조회할 때, Member와 연관 관계에 있는 엔티티 중 어떤 엔티티를 함께 fetch join 해올것인지를 지정


      //Spring Data JPA로 Fetch Join 구현 3
      @EntityGraph("Member.all")
      @Query("select m from Member m")
      List<Member> findMemberEntityGraph(); // 참고로, 이 메서드 명은 Spring Data JPA의 쿼리 메서드의 형식을 지키고 있지 않기에 쿼리 메서드 기능은 사용 X

      //Spring Data JPA로 Fetch Join 구현 4
      @EntityGraph(attributePaths = {"team"})
      List<Member> findEntityGraphByUsername(@Param("username") String username); //이건 쿼리 메서드 기능을 사용하고 있다.


     // @QueryHint[s]는 Spring Data JPA가 제공하는 에노테이션이고,
     // @QueryHint(name = "asdfads...)는 [하이버네이트]가 제공하는 에노테이션이다.
      @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
      Member findReadOnlyByUsername(String username);


      // @Lock : Spring Data JPA에서 제공하는 에노테이션
      // LockModeType.PESSIMISTIC_WRITE : JPA에서 제공하는 enum 타입입

      @Lock(LockModeType.PESSIMISTIC_WRITE)
      List<Member> findLockByUsername(String username);


      // Projectino 기능 사용(반환형의 엔티티의 속성들이 projection의 대상이 돼서, 엔티티에 조회 결과가 삽입돼서 반환된다.)
       List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

       // Proxy 객체가 아닌, UsernameOnlyDto 엔티티로 반환!!
      List<UsernameOnlyDto> findProjectByUsername(@Param("username") String username);


      // Generic으로 해서, UsernameOnly으로 반환 받고 싶으면, UsernameOnly 타입을 매개변수로 넘기고,
      // UsernameOnlyDto로 반환을 받고 싶으면, UsernameOnlyDto 타입을 매개변수로 넘기면 된다.
      // -> 동적으로 반환받고 싶은 엔티티 타입을 정의할 수가 있다.
      <T> List<T> findProByUsername(@Param("username") String username,Class<T> type);

}
