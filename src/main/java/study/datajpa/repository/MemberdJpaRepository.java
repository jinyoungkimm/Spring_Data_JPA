package study.datajpa.repository;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

@Repository
@RequiredArgsConstructor
public class MemberdJpaRepository {

    @Autowired
    private final EntityManager entityManager;

    public Member save(Member member){

        entityManager.persist(member);
        return member;
    }

    public Member find(Long memberId){

        return entityManager.find(Member.class,memberId);

    }

}
