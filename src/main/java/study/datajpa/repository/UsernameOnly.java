package study.datajpa.repository;


import org.springframework.beans.factory.annotation.Value;

// 전체 엔티티를 조회하는 것이 아니라, select의 projection의 대상을 지정해서 들고 오고 싶을 때 Projections 기능 사용
public interface UsernameOnly {
    // 여기서는 Member 엔티티의 username, 딱 1개만을 지정해서 들고 오는 시나리오!

    @Value("#{target.username + ' ' + target.age}")
    String getUsername(); //이와 같은 형식(getter+필드명)으로 선언을 해주면 , Spring Data JPA가
    // "아!! USERNAME만 SELECT하면 되는구나"라고 판단하여, 해당 쿼리 구현체를 자동으로 만들어서,
    // UsernameOnly Proxy 객체에 username이 담긴 엔티티를 반환을 한다.

}
