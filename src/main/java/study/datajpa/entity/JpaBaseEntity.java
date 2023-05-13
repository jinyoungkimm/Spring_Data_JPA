package study.datajpa.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


// 이 정보를 [공통적]으로 필요로 하는 [엔티티]에 상속을 시키면 된다.
@MappedSuperclass // 이 클래스는 진짜 [부모] 클래스로서 상속시키는 것이 아니라, 그냥 속성만 내려 받겠다는 에노테이션!
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) // createdDate는 [수정]이 불가능!
    private LocalDateTime createdDate; // 등록일
    private LocalDateTime updatedDate; // 수정일일

    @PrePersist // JPA에서 persist()가 호출이 되면, 그 전에 @PrePersist가 붙은 메서드가 실해이 된다.
    public void prePersist(){

        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now; // 최초 등록일은 또 다른 시각으로는 최초 [수정일]로도 볼 수가 있다.
                                // 무엇보다 sql문에 null이 들어 가는 것은 그렇게 좋지가 않다고 한다.
    }


    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }




}