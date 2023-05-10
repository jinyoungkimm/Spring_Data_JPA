package study.datajpa.repository;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberdJpaRepository {

    @Autowired
    private final EntityManager entityManager;

    public Member save(Member member){

        entityManager.persist(member);
        return member;
    }

    public void delete(Member member){
        // JPA는 remove()라는 메서드를 제공하며,
        entityManager.remove(member);
    }

    //전체 조회
    public List<Member> findAll(){

        return entityManager.createQuery("select m from Member m",Member.class)
                .getResultList();

    }


    //단건 조회
    public Member find(Long memberId){

        return entityManager.find(Member.class,memberId);

    }
    //위 find() 메서드와 기능이 똑같지만, 반환형을 Optional 클래스로 해 줬을 뿐!
    public Optional<Member> findById(Long id){

        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member); // member 객체를 Optional 객체로 한 번 Wrapping한다.
    }

    //조회된 객체의 개수
    public Long count(){

        return entityManager.createQuery("select count(m) from Member m",Long.class)
                .getSingleResult();

    }

}
