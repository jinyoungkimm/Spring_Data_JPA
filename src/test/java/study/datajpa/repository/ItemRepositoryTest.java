package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save(){

        // 새로운 엔티티
      /*  Item item = new Item(); // 이 시점의 Item 엔티티의 식별자(id)는 [null] -> DB에 저장돼 있지 않은 [새로운 엔티티]
        itemRepository.save(item); // em.persist(item)로 저장됨.*/

        // 새로운 엔티티 x
        Item item = new Item("AAA"); // 이미 식별자(id)가 존재(null이 아님) -> 이미 DB에 저장돼 있는 엔티티라고 판단.
        itemRepository.save(item); // Spring Data Jpa는 em.[merge(ite)]으로 저장(이때 치명적인 단점이,
                                   // DB에 이미 저장돼 있는 새로운 엔티티가 아닌 엔티티이므로, SELECT쿼리를 1번 날린후,
                                    // 트랜잭션이 종료되면, INSERT문이 날라 간다.(SELECT문이 1번 날라간다는 치명적인 단점!)


    }

}

