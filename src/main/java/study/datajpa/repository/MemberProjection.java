package study.datajpa.repository;


//DTO(스프링 데이터 인터페이스 Projcetion)
public interface MemberProjection {

    //Member
    Long getId();

    String getUsername();

    //Team
    String getTeamName();


}
