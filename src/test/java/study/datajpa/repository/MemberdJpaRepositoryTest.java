package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberdJpaRepositoryTest {

    @Autowired
    private MemberdJpaRepository repository;


    @Test
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





}