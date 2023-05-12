package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
@Rollback(value = false)
class MemberReposiotryTest {

    @Autowired
    MemberReposiotry repository; // Spring Data JPA를 설정한 인터페이스!(구현체는 Spring Data JPA가 자동 생성해서 주입)

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager entityManager; // 같은 Transaction이면 같은 엔티티 매니저로 동작
                                 // -> 혹시 Transaction이 생성될 때마다, 다른 엔티티 매니저가 할당되나???
    @Test
    public void testMember(){

        //givien
        Member member = new Member("memberA");

        //when
        Member savedMember = repository.save(member);

        // Spring Data JPA에서는 Optional로 반환을 한다.
        // -> id로 객체가 조회되지 않을 수도 있으니깐!!!
        Optional<Member> findMember = repository.findById(savedMember.getId());

        //then
        assertThat(findMember.get().getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember.get().getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.get()).isEqualTo(savedMember);

    }

    @Test
    @Transactional(readOnly = false)
    @Rollback(value = false)
    public void basicCRUD(){

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        repository.save(member1); // Spring Data JPA에서 제공하는 SAVE()이다.
        repository.save(member2);

        Optional<Member> findMember1 = repository.findById(member1.getId());  // Spring Data JPA에서 제공하는 findByID()이다.
        Optional<Member> findMember2 = repository.findById(member2.getId());
        //단일 조회 검증
        assertThat(findMember1.get()).isEqualTo(member1);
        assertThat(findMember2.get()).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> all = repository.findAll();  // Spring Data JPA에서 제공하는 FindAll()이다.
        assertThat(all.size()).isEqualTo(2);

        //삭제 검증
        repository.delete(member1);  // Spring Data JPA에서 제공하는 DELETE이다.
        repository.delete(member2);

        long delete_count = repository.count();
        assertThat(delete_count).isEqualTo(0);
    }

    @Test@Transactional
    public void findByusernameAndAgeGreaterThan(){

        Member member1 = new Member("member1", 19);
        Member member2 = new Member("member1", 20);
        Member member3 = new Member("member2", 21);
        Member member4 = new Member("member3", 22);
        Member member5 = new Member("member1", 23);
        repository.save(member1); // repository는 MemberRepository [인터페이스]이다!
        repository.save(member2);
        repository.save(member3);
        repository.save(member4);
        repository.save(member5);

        List<Member> findMember = repository.findByusernameAndAgeGreaterThan("member1",20);

        for (Member member : findMember) {
            assertThat(member.getUsername()).isEqualTo("member1");
            assertThat(member.getAge()).isGreaterThan(20);
        }

        assertThat(findMember.size()).isEqualTo(1);

    }

    @Test@Transactional
    public void testNamedQuery() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<Member> findMember = repository.findByUsername("member1");


        Member member = findMember.get(0);
        assertThat(member).isEqualTo(member1);

    }

    @Test@Transactional
    public void testQuery() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<Member> findMember = repository.findUser("member1",10);

        Member member = findMember.get(0);
        assertThat(member).isEqualTo(member1);

    }

    @Test@Transactional
    public void findUsernameList() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<String> findMember = repository.findUsernameList();

        for (String s : findMember) {
            System.out.println("s = " + s);
        }

    }

    @Test@Transactional
    public void findMemberDto() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("member1", 10);
        member.setTeam(team);
        repository.save(member);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<MemberDto> findMember = repository.findMemberDto();
        for (MemberDto memberDto : findMember) {
            System.out.println("memberDto = " + memberDto); // lombok이 toString()을 자동으로 오버라이딩해주었음.
        }


    }

    @Test@Transactional
    public void findByNames() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // [Spring Data JPA]가 제공하는 @Query로 쿼리 메서드 [자동] 생성!!!
        List<Member> findMember = repository.findByNames(Arrays.asList("member1","member2"));
        for (Member member : findMember) {
            System.out.println("member = " + member); // Member 클래스의 @ToString()에 의해 자동으로 오버라이딩됨.
        }
    }

    @Test@Transactional
    public void returnTypes() {

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        repository.save(member1);
        repository.save(member2);

        // Spring Data JPA은 컬렉션을 반환 타입으로 지원!
        List<Member> findMembers = repository.findListByUsername("member1");
        //만약, "member1"이 DB에 없다면, List로 NULL이 반환이 될까??
        //->NOPE!!! [빈 컬렉션], List 같은 경우, new ArrayList<>()를 반환한다.
        // 고로 if( findMembers == null ), else... 뭐 이런 코드는 안 짜도 된다.
        // (JPA는 해당 쿼리에 대한 결과가 없어도, List에 NULL값을 반환하지 않는다는 스펙이 있다)

        // 만약 username이 UNIQUE하다면 굳이 컬렉션(List)로 받을 필요가 없다.
        Member findMember = repository.findMemberByUsername("member1");
        // 만약 username에 해당하는 Member가 없을 수도 있는 경우에는 NULL을 반환을 한다.
        //-> 순수 JPA는 NoResultException을 터트려서 버리지만,
        //Spring Data JPA는 Exception을 터트리지 않고, try-catch를 내부적으로 돌려서 null을 반환한다.

        //실제로 Excpetion을 터트리는 것이 낫냐, 아니면 null을 반환하는 것이 낫냐라는 논쟁이 있었다.
        // 그러나 JAVA 8에서부터 Optional이 나오면서 이 논쟁은 사라졌다.
        // -> 1개의 객체에 대한 조회 결과가 있을 수도, 없을 수도 있는 경우에는 [Optinal]을 사용하자!
        Optional<Member> member3 = repository.findOptionalByUsername("member3");
        //근데, 위 코드에서 만약 조회의 결과가 단건이 아니라 2개 이상이면??
        //->그때는 Optional이라도 예외가 터진다.
        // Spring Data JPA는 이때에 InCorrectResultSizeDataAccessException이라는 [Spring]에서 정의된 Excpetion으로 바꿔서 예외를 터트림.
        // 우리는 현재 Repository는 JPA로 구현을 하였지만, Mongo로 Repository를 구현할 수도 있고, 다른 방식으로 Repository를 구현할 수가 있다.
        // 그러나 Spring은 그 어떠한 방법으로 구현을 하였다고 하여도, 조회 결과가 2개이상인 것에 대해서는 모두 [InCorrectResultSizeDataAccessException]
        // 으로 반환을 한다.
        // -> 장점 : Service 계층에서 JPA_Repository를 사용하다가, MONGO_Repository로 변경을 하였을 때, [똑같은] 예외가 올라오므로
        // 예외 처리에 대한 [클라이언트 코드의 수정을 하지 않아도 된다](OCP 원칙)


    }

    @Test@Transactional
    public void pagin(){

        /**
         *  1. Page 사용 예시
         */

        /*repository.save(new Member("member1",10));
        repository.save(new Member("member2",10));
        repository.save(new Member("member3",10));
        repository.save(new Member("member4",10));
        repository.save(new Member("member5",10));

        int age =10;

        // PageRequest는 인터페이스인 Pageable의 구현체!!!
        // JPA는 첫 페이지가 1부터 시작, Spring Data JPA는 첫 페이지가 0부터 시작!!(주의!!)
        // [PageRequest]는 [page]인터페이스의 구현체!!!
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));


        // 쿼리 메서드용 SQL문 1번, totalCount용 SQL문 1번 : 총 2번의 쿼리리
       Page<Member> page = repository.findByAge(age, pageRequest); // findByAge()를 JPQL로 구현하지 않아도, 자동으로 생성해줌


        //위 코드에서 페이징 처리된 객체(3개)를 반환한다.
        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member); //@ToString()이 toString()을 자동으로 오버라이딩해 줬음!!!
        }

        long totalCount = page.getTotalElements(); // totalCount 반환!
        System.out.println("totalCount = " + totalCount);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);

        assertThat(page.getNumber()).isEqualTo(0);

         assertThat(page.isFirst()).isTrue();
         assertThat(page.hasNext()).isTrue();*/

        /**
         * 2. Slice 사용 예시 : 이건 [페이징] 기능을 하는 것이 전혀 아니다.
         * -> 다른 곳에서 [페이징] SQL문을 날린 후, 1개의 튜플들을 더 들고 오는 것에 불과하다.
         */

       /* repository.save(new Member("member1",10));
        repository.save(new Member("member2",10));
        repository.save(new Member("member3",10));
        repository.save(new Member("member4",10));
        repository.save(new Member("member5",10));

        int age =10;

        // [페이징] 요청은 여기서 일어남.
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));

        // 참고로, Slice 클래스는 PageRequest 클래스보다 더 상위(부모 or 조상 클래스)이기에, findPageByAge()의 반환형이
        // Page(정확히는 PageRequest)여도 업캐스팅돼서, 받을 수는 있다.
        // 내부적으로 limit [ + 1 ]을 하여 1개의 튜플을 추가적으로 더 들고 온다.
        Slice<Member> page = repository.findPageByAge(age, pageRequest); // findPageByAge()를 JPQL로 구현하지 않아도, 자동으로 생성해줌

        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member); //@ToString()이 toString()을 자동으로 오버라이딩해 줬음!!!
        }

    //    long totalCount = page.getTotalElements();  Slice에는 이런 기능이 없다.
    //    System.out.println("totalCount = " + totalCount);

        assertThat(content.size()).isEqualTo(3);

      //assertThat(page.getTotalElements()).isEqualTo(5); Slice에는 이런 기능이 없다.

        assertThat(page.getNumber()).isEqualTo(0);

        //assertThat(page.getTotalPages()).isEqualTo(2); Slice에는 이런 기능이 없다.

        // 총 2개의 페이지가 반환된다(page 0, page 1)
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
*/
        /**
         *  Slice가 사용되는 예
         *  -> 모바일 디바이스에서 보면 아래로 쭉 스크롤을 하다가, [더보기]란이 있다.
         *  이거의 원리는 처음에 1개의 페이지를 보여주고, Slice가 만약 DB에 1개의 TUPLE이 추가로 남아 있으면 그것을 들고 온다.
         *  그렇게 추가의 튜플이 있으면, [더보기]란을 띄워주고, 없으면, 안 띄워 준다.
         *  -> 만약 Page로 했는데, totalCount가 너무 많으면, Slice 방식을 이용하여 최적화하는 방법도 있다.
         */



        repository.save(new Member("member1",10));
        repository.save(new Member("member2",10));
        repository.save(new Member("member3",10));
        repository.save(new Member("member4",10));
        repository.save(new Member("member5",10));

        int age =10;


        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));


        // 쿼리 메서드용 SQL문 1번, totalCount용 SQL문 1번 : 총 2번의 쿼리문 날림
        // Page는 outer Join을 사용하여 totalCount를 계산한다.
        // 그러나 만약 table이 100개 라면 수많은 join이 일어나면서 성능에 문제가 생긴다.
        // findPageByAge()를 @Query(countQuERY = "SELECT COUNT() ~~ "으로 JOIN 연산 없이 최적화하였다.
        // 실행 결과를 확인해라(게시물에서도 확인 가능)
        Page<Member> page = repository.findtotalCountByAge(age, pageRequest); // findByAge()를 JPQL로 구현하지 않아도, 자동으로 생성해줌

        // [페이징을 유지하면서] 손쉽게 Dto로 변환하기 (feat. map() )
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        // 만약 API 호출 결과를 페이징을 유지하면서 반환해야 한다면, map()을 사용해서 손쉽게 Dto로 변환해서 반환하면 된다.
        // (실무 꿀팁이라고 하심)


        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member); //@ToString()이 toString()을 자동으로 오버라이딩해 줬음!!!
        }

        long totalCount = page.getTotalElements(); // totalCount 반환!

        System.out.println("totalCount = " + totalCount);

        assertThat(content.size()).isEqualTo(3);

        assertThat(page.getTotalElements()).isEqualTo(5);

        assertThat(page.getNumber()).isEqualTo(0);

        // 총 2개의 페이지가 반환된다(page 0, page 1)
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }


    @Test@Transactional
    public void bulkUpdate(){

        repository.save(new Member("member1",10));
        repository.save(new Member("member2",19));
        repository.save(new Member("member3",20));
        repository.save(new Member("member4",21));
        repository.save(new Member("member5",40));


        int updated_row = repository.bulkAgePlus(20);// 20살 이상인 사람의 나이를 +1만큼 증가!
        //entityManager.clear(); // 벌크 연산 시, 꼭 Context를 초기화시켜 줘야 데이터 불일치 같은 문제가 안 생긴다.


        // Context가 clear()된 것은 아니기에, member5를 조회를 하면 +1이 반연되지 않은 40살이 조회된다.
        // 그러나 DB에는 이미 41살로 업데이트가 완료가 됨 -> [데이터 불일치] 문제
        // JPA의 AUTO COMMIT 전략 중 하나로, ExecuteUpdate()처럼 즉시, 쿼리문이 날라가는 경우
        // -> JPA가 먼저 Context에 있는 모든 sql문을 flush()를 해주고, 그 다음 update문을 날리게 된다.
        List<Member> member5 = repository.findByUsername("member5");
        Member member = member5.get(0);
        System.out.println("member = " + member);  // 40살이 출력됨.


        assertThat(updated_row).isEqualTo(3);

    }

    @Test@Transactional
    public void findMemberLazy() throws Exception {


        //given

        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        repository.save(new Member("member1", 10, teamA));
        repository.save(new Member("member2", 20, teamB));

        entityManager.flush();
        entityManager.clear();


        //when
        List<Member> members = repository.findAll(); // @EntityGraph(attributespath = {"team}으로 (n+1) 문제 해결!

        //then
        for (Member member : members)
            member.getTeam().getName();
    }


    @Test@Transactional
    public void queryHint(){

        // hint 적용 전

        /*Member member = repository.save(new Member("member1", 10));
        entityManager.flush();
        entityManager.clear();


        Optional<Member> optional = repository.findById(member.getId()); // Context를 clear()하였으므로, DB에서 조회를 한다.
        Member findMember = optional.get();
        // "member1" -> "member2"로 변경(dirty checking 기법으로 변경)
        findMember.setUsername("member2");
        entityManager.flush();

        // member2로 출력됨.
        System.out.println("findMember = " + findMember);*/


        // hint 적용 후


        Member member = repository.save(new Member("member1", 10));
        entityManager.flush();
        entityManager.clear();

        // DB에서 조회한 뒤, Context에 저장될 때, 원본 객체 딱 1개만 저장이 된다.
        Member findMember = repository.findReadOnlyByUsername(member.getUsername());
        findMember.setUsername("member2");
        entityManager.flush();
    }


    @Test@Transactional
    public void queryLock(){

        Member member = repository.save(new Member("member1", 10));
        entityManager.flush();
        entityManager.clear();

        List<Member> findMember = repository.findLockByUsername("member1");

    }

    @Test@Transactional
    public void callCustom(){

        List<Member> memberCustom = repository.findMemberCustom();

    }





}