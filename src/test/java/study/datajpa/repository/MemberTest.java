package study.datajpa.repository;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional(readOnly = true)
public class MemberTest {

    @Autowired
    EntityManager entityManager;


    @Test
    public void testEntity(){

        //givien
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        // TEAM 엔티티를 생성자로 주입!
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        // 아래의 JPQL을 사용하기 위해서는, Context의 쿼리문을 한 번 db에 날려 줘야만 한다.
        entityManager.flush();
        // 아래의 JPQL의 결과가 Context에 의해 1차 캐싱된 것이 아닌, DB로부터 조회된 것임을 확실히 하기 위함!
        entityManager.clear();


        List<Member> members = entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {

            System.out.println("member=" + member);

            System.out.println("-> member.team=" + member.getTeam()); // Team Proxy가 초기화!(N+1)문제 발생함!

        }
    }


    }


