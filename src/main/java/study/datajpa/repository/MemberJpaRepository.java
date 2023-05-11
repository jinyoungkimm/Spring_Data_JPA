package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

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

    // 순수 JPA로 [username]과 [age]라는 특정 비지니스의 [도메인 엔티티]를 가지고 쿼리 메서드([조회] 메서드)를 만든 것이다.
    // -> Spring Data JPA는 [스프링 데이터]와 [스프링 데이터 JPA] 계층에서는 제공하지 않는 쿼리 메서드([조회] 메서드)를 순수 JPA보다
    // 훨씬 더 간편하게 만들 수가 있다. ("쿼리 메서드" 게시물 참조)
    public List<Member> findByusernameAndAgeGreaterThan(String username,int age){

        return entityManager.createQuery("SELECT m FROM Member m WHERE m.username = :username AND m.age > :age")
                .setParameter("username",username)
                .setParameter("age",age)
                .getResultList();

    }

    public List<Member> findByUsername(String username){
        // createQuery x -> createNamedQuery o
       return entityManager.createNamedQuery("Member.findByUsername",Member.class)
                .setParameter("username",username)
                .getResultList();
    }

}
