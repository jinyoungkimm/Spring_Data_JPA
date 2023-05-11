package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

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



}