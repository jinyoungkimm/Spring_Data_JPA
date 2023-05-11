package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional(readOnly = true)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository repository;


    @Test
    @Transactional(readOnly = false)
    //@Rollback(value = false)
    public void testMember(){

        //givien
        Member member = new Member("memberA");

        //when
        Member savedMember = repository.save(member);
        Member findedMember = repository.find(savedMember.getId());

        //then
        assertThat(findedMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findedMember.getId()).isEqualTo(savedMember.getId());
        //JPA에서는 같은 ID로 Context로 조회한 객체들은 [무조건] 같은 객체, 즉 참조변수 값이 같음을 보장한다.
        assertThat(findedMember).isEqualTo(savedMember);

    }

    @Test
    @Transactional(readOnly = false)
    //@Rollback(value = false)
    public void basicCRUD(){

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        repository.save(member1);
        repository.save(member2);

        Optional<Member> findMember1 = repository.findById(member1.getId());
        Optional<Member> findMember2 = repository.findById(member2.getId());
        //단일 조회 검증
        assertThat(findMember1.get()).isEqualTo(member1);
        assertThat(findMember2.get()).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> all = repository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //삭제 검증
        repository.delete(member1);
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
        repository.save(member1);
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

        List<Member> findMember = repository.findByUsername("member1");


        Member member = findMember.get(0);
        assertThat(member).isEqualTo(member1);



    }

    @Test@Transactional
    public void pagin(){

        repository.save(new Member("member1",10));
        repository.save(new Member("member2",10));
        repository.save(new Member("member3",10));
        repository.save(new Member("member4",10));
        repository.save(new Member("member5",10));

        int age =10;
        int offset = 1;
        int limit = 3;
        List<Member> findPaging = repository.findByPage(age, offset, limit);
        long totalCount = repository.totalCount(age);

        assertThat(findPaging.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);

    }


}