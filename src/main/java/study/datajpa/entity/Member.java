package study.datajpa.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class Member {

    @Id@GeneratedValue
    private Long id;

    private String username;

    // JPA에서는 꼭 기본 생성자가 있어야 한다.
    // protected로 한 이유 : 개발자가 클라이언트 코드에서는 사옹되지 못하게 하기 위함!!
    // 오로지, JPA에서 내부적으로 동작할 때만 기본 생성자가 호출이 된다.
    protected Member(){}

    public Member(String username){

        this.username = username;
    }

}
