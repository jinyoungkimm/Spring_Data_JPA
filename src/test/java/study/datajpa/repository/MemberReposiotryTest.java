package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberReposiotryTest {

    @Autowired
    MemberReposiotry repository; // Spring Data JPA를 설정한 인터페이스!(구현체는 Spring Data JPA가 자동 생성해서 주입)


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

}