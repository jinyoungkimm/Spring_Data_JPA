package study.datajpa.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> { // 게시물 참조!

   /* @Id@GeneratedValue
    Long id;*/

    @Id
    String id;

    @CreatedDate
    private LocalDateTime createdTime;

    public Item(String id){
        this.id = id;
    }

    public Item(){

    }

    @Override
    public boolean isNew() {
        return createdTime == null;
    }
}
