package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberReposiotry;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberReposiotry memberReposiotry;

    @GetMapping("/membersV1/{id}")
    public String findMemberv1(@PathVariable("id") Long id){ // 그러나, PK값을 외부에 노출시키는 것은 그렇게 좋은 것은 아니다.

        Optional<Member> findMember = memberReposiotry.findById(id);
        Member member = findMember.get();

        return member.getUsername();

    }

    @GetMapping("/membersV2/{id}")
    public String findMemberv2(@PathVariable("id") Member member){ // PK인 ID를 받아서 [바로] 해당 엔티티로 반환!
                                                                    // PK값을 받아서 해당 객체를 찾는 [단순한] API일 때는 컨버터를 사용해도 괜찮지만,
                                                                    // 사실 실무에서 PK를 받아서 객체를 찾는 그런 단순한 API는 잘 없다.
                                                                     // 대부분의 경우, PK 값을 받아서 여러 로직들을 짜주는 경우가 대부분이다.
        // -> [도메인 엔티티]로 API 개발을 절대 해서는 안된다.
        // 만약에 [도메인 엔티티]로 API 개발을 한다고 하면
        // 딱 [조회] 용으로만 사용해야 한다.(id값으로, 해당 객체를 [조회]하는 용도로만 사용!!)
        return member.getUsername();

    }


    // page의 default값( [ Local Setting ] )
    // sort는 default값 오름차순(asc)이기에 적어 주지 않아도 된다.
    // 내리차순이면 [명시적]으로 desc를 적어 줘야 한다.
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5, sort="username") Pageable pageable){ // Page, Pagealbe 모두 [인터페이스]이다.

        Page<Member> page = memberReposiotry.findAll(pageable);
        //손쉽게 Page를 DTO로 변환하는 꿀팁(PAGE.map()이용)
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(),  member.getUsername(),null));
        return map;

    }




   // @PostConstruct
    public void init(){

        for(int i =0 ; i < 100; i++){

            memberReposiotry.save(new Member("user"+i,i));

        }

    }



}
