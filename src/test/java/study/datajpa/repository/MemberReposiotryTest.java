package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
@Rollback(value = false)
class MemberReposiotryTest {

    @Autowired
    MemberReposiotry repository; // Spring Data JPA를 설정한 인터페이스!(구현체는 Spring Data JPA가 자동 생성해서 주입)

    @Autowired
    TeamRepository teamRepository;
    @Test
    public void testMember(){

        //givien
        Member member = new Member("memberA");

        //when
        Member savedMember = repository.save(member);

        // Spring Data JPA에서는 Optional로 반환을 한다.
        // -> id로 객체가 조회되지 않을 수도 있으니깐!!!
        Optional<Member> findMember = repository.findById(savedMember.getId());

        //then
        assertThat(findMember.get().getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember.get().getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.get()).isEqualTo(savedMember);

    }

    @Test
    @Transactional(readOnly = false)
    @Rollback(value = false)
    public void basicCRUD(){

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        repository.save(member1); // Spring Data JPA에서 제공하는 SAVE()이다.
        repository.save(member2);

        Optional<Member> findMember1 = repository.findById(member1.getId());  // Spring Data JPA에서 제공하는 findByID()이다.
        Optional<Member> findMember2 = repository.findById(member2.getId());
        //단일 조회 검증
        assertThat(findMember1.get()).isEqualTo(member1);
        assertThat(findMember2.get()).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> all = repository.findAll();  // Spring Data JPA에서 제공하는 FindAll()이다.
        assertThat(all.size()).isEqualTo(2);

        //삭제 검증
        repository.delete(member1);  // Spring Data JPA에서 제공하는 DELETE이다.
        repository.delete(member2);

        long delete_count = repository.count();
        assertThat(delete_count).isEqualTo(0);
    }

    @Test@Transactional
    public void findByusernameAndAgeGreaterThan(){

        Member member1 = new Member("member1", 19);
        Member member2 = new Member("member1", 20);
        Member member3 = new Member("member2", 21);
        Member member4 = new Member("member3", 22);
        Member member5 = new Member("member1", 23);
        repository.save(member1); // repository는 MemberRepository [인터페이스]이다!
        repository.save(member2);
        repository.save(member3);
        repository.save(member4);
        repository.save(member5);

        List<Member> findMember = repository.findByusernameAndAgeGreaterThan("member1",20);

        for (Member member : findMember) {
            assertThat(member.getUsername()).isEqualTo("member1");
            assertThat(member.getAge()).isGreaterThan(20);
        }

        assertThat(findMember.size()).isEqualTo(1);

    }

    @Test@Transactional
    public void testNamedQuery() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<Member> findMember = repository.findByUsername("member1");


        Member member = findMember.get(0);
        assertThat(member).isEqualTo(member1);

    }

    @Test@Transactional
    public void testQuery() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<Member> findMember = repository.findUser("member1",10);

        Member member = findMember.get(0);
        assertThat(member).isEqualTo(member1);

    }

    @Test@Transactional
    public void findUsernameList() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<String> findMember = repository.findUsernameList();

        for (String s : findMember) {
            System.out.println("s = " + s);
        }

    }

    @Test@Transactional
    public void findMemberDto() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("member1", 10);
        member.setTeam(team);
        repository.save(member);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<MemberDto> findMember = repository.findMemberDto();
        for (MemberDto memberDto : findMember) {
            System.out.println("memberDto = " + memberDto); // lombok이 toString()을 자동으로 오버라이딩해주었음.
        }


    }

    @Test@Transactional
    public void findByNames() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<Member> findMember = repository.findByNames(Arrays.asList("member1","member2"));
        for (Member member : findMember) {
            System.out.println("member = " + member); // Member 클래스의 @ToString()에 의해 자동으로 오버라이딩됨.
        }
    }

    @Test@Transactional
    public void returnTypes() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // Spring Data JPA은 컬렉션을 반환 타입으로 지원!
        List<Member> findMembers = repository.findListByUsernameS("member1");
        //만약, "member1"이 DB에 없다면, List로 NULL이 반환이 될까??
        //->NOPE!!! [빈 컬렉션], List 같은 경우, new ArrayList<>()를 반환한다.
        // 고로 if( findMembers == null ), else... 뭐 이런 코드는 안 짜도 된다.
        // (JPA는 해당 쿼리에 대한 결과가 없어도, List에 NULL값을 반환하지 않는다는 스펙이 있다)

        // 만약 username이 UNIQUE하다면 굳이 컬렉션(List)로 받을 필요가 없다.
        Member findMember = repository.findMemberByUsername("member1");
        // 만약 username에 해당하는 Member가 없을 수도 있는 경우에는 NULL을 반환을 한다.
        //-> 순수 JPA는 NoResultException을 터트려서 버리지만,
        //Spring Data JPA는 Exception을 터트리지 않고, try-catch를 내부적으로 돌려서 null을 반환한다.

        //실제로 Excpetion을 터트리는 것이 낫냐, 아니면 null을 반환하는 것이 낫냐라는 논쟁이 있었다.
        // 그러나 JAVA 8에서부터 Optional이 나오면서 이 논쟁은 사라졌다.
        // -> 1개의 객체에 대한 조회 결과가 있을 수도, 없을 수도 있는 경우에는 [Optinal]을 사용하자!
        Optional<Member> member3 = repository.findOptionalByUsername("member3");
        //근데, 위 코드에서 만약 조회의 결과가 단건이 아니라 2개 이상이면??
        //->그때는 Optional이라도 예외가 터진다.
        // Spring Data JPA는 이때에 InCorrectResultSizeDataAccessException이라는 [Spring]에서 정의된 Excpetion으로 바꿔서 예외를 터트림.
        // 우리는 현재 Repository는 JPA로 구현을 하였지만, Mongo로 Repository를 구현할 수도 있고, 다른 방식으로 Repository를 구현할 수가 있다.
        // 그러나 Spring은 그 어떠한 방법으로 구현을 하였다고 하여도, 조회 결과가 2개이상인 것에 대해서는 모두 [InCorrectResultSizeDataAccessException]
        // 으로 반환을 한다.
        // -> 장점 : Service 계층에서 JPA_Repository를 사용하다가, MONGO_Repository로 변경을 하였을 때, [똑같은] 예외가 올라오므로
        // 예외 처리에 대한 [클라이언트 코드의 수정을 하지 않아도 된다](OCP 원칙)


    }


}