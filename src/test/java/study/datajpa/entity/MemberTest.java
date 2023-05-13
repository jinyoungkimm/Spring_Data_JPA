package study.datajpa.entity;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberReposiotry;

@SpringBootTest
class MemberTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberReposiotry memberReposiotry;


    @Test
    @Transactional
    void JpaEventBaseEntity() throws Exception {

        //givien
        Member member = new Member("member1");
        memberReposiotry.save(member); // persist() 실행 전에, @PrePersist가 붙은 메서드가 실행(Auditing이 실행)

        Thread.sleep(100);

        member.setUsername("member2"); //Dirty Checking으로 Update됨을 감지!
        entityManager.flush(); // @PreUpdate가 실행된다.(Auditing이 실행)

        entityManager.clear();

        //when
        Member member1 = memberReposiotry.findById(member.getId()).get();


        //then
        System.out.println("member1 getCreatedDate() = " + member1.getCreatedDate());
        System.out.println("member1.getLastModifiedDate() = " + member1.getLastModifiedDate());
        System.out.println("member1.getCreatedBy() = " + member1.getCreatedBy());
        System.out.println("member1.getLastModifiedBy() = " + member1.getLastModifiedBy());


    }




}
