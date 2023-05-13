package study.datajpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // 이 [엔티티]는 Event가 Auditing일 때 사용된다는 것을 알려주는 것으로,
                                               // 꼭 넣어 줘야 한다.
@MappedSuperclass
@Getter
public class BaseEntity { // Spring Data JPA가 제공하는 Auditing 기능!

    @CreatedDate // Spring Data Jpa가 제공하는 기능으로써, 메서드를 구현하지 않아도 된다.
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate // Spring Data Jpa가 제공하는 기능으로써, 메서드를 구현하지 않아도 된다.
    private LocalDateTime lastModifiedDate;

    // 여기까지는 순수하게 JPA만으로 구현한 JpaBaseEntity와 똑같이 동작을 한다.!
    // 여기에서부터는 등록자, 수정자에 대한 정보를 추가할 것이다.
    // 아래의 등록자, 수정자 기능을 이용하려먼, main 메서드 부분에서 Bean을 등록해 줘야 한다.
    // 그 빈은 등록자, 수정자의 정보를 제공하는 빈이다.
    // Spring Data JPA는  등록 , 수정이 일어날 때마다, 그 빈을 호출하여 등록자,수정자 정보(보통의 경우, 회원의 id)
    // 를 가져와서, 아래 필드에 삽입을 한다.

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(updatable=false)
    private String lastModifiedBy;




}
