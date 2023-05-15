package study.datajpa.repository;

public class UsernameOnlyDto {


    private final String username;

    // 이 생성자가 중요!
    public UsernameOnlyDto(String username){ // Spring Data JPA는 이 생성자의 매개변수 명(username)을
                                            //  Member 엔티티의 projection 대상이라고 보고, 결과를 UsernameOnlyDto에 담아서 반환을 한다.
                                           // 고로, 생성자의 매개변수명을 Member 엔티티의 필드값과 다르게 적으면 안됨.
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }



}
