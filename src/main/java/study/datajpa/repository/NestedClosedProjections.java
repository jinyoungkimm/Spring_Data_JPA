package study.datajpa.repository;


// 중첩 구조(인터페이스 안에 또 다른 인터페이스, 즉 Proxy [객체] 안에, 또 다른 Proxy [객체]가 삽입된 반환형)
public interface NestedClosedProjections {


    String getUsername(); // username에 대해서는 최적화가 된다.( 쿼리문을 보면, username만이 select의 projection 대상이 된다.
    TeamInfo getTeam(); // 중첩 구조 안의 엔티티에 대해서는 최적화가 되지 않아서, 쿼리문을 살펴 보면, Team 엔티티의 [모든] 필드를 Projection해서
                        // 들고 온 뒤에, getName()을 통해서, team 이름을 엔티티로 삽입하여 반환을 한다.

   public interface TeamInfo{

        String getName(); // team 이름
    }


}
