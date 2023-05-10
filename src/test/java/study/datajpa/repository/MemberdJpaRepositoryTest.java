package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class MemberdJpaRepositoryTest {

    @Autowired
    private MemberdJpaRepository repository;


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
    @Rollback(value = false)
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
}