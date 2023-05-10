package study.datajpa.repository;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager entityManager;

    //여기 아래에는 [수정]에 대한 메서드가 없다.
    //-> 필요 없다. EntityManager로부터 수정할 객체를 조회해 와서, setter를 사용하면
    // 자동으로 TX 종료 시점에 [수정]이 되기 때문이다.


    public Team save(Team team){

        entityManager.persist(team);
        return team;
    }

    public void delete(Team team){
        entityManager.remove(team);
    }

    public List<Team> findAll(){
        return entityManager.createQuery("select t FROM Team t",Team.class)
                .getResultList();
    }

    // 단일 조회
    public Team find(Long id){
        Team team = entityManager.find(Team.class, id);
        return team;
    }

    // 단일 조회 with Optional
    public Optional<Team> findById(Long id){

        Team team = entityManager.find(Team.class, id);
        return Optional.ofNullable(team);

    }

    public Long count(){

        return entityManager.createQuery("SELECT count(t) FROM Team t",Long.class)
                .getSingleResult();
    }

}
