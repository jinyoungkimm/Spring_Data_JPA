package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 Protected로 자동 생성
@ToString(of = {"id", "name"})
public class Team extends BaseEntity { // 등록일, 수정일에 대한 정보가 필요하므로, JpaBaseEntity를 상속 받았다.

    @Id@GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team") // @~ToMany는 default가 [즉시 지연]
    List<Member> members = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }

}
