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
import study.datajpa.entitiy.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}") //도메인 클래스 컨버터 위 소스를 객체로 받음
    public String findMember(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5, sort = "username") Pageable pageable){

        Page<Member> page = memberRepository.findAll(pageable);
        page.map(MemberDto::new); // entity를 DTO로 변환 API 응답할 때 필요
        return page;
    }

    @PostConstruct
    public void init(){
        for(int i = 0; i < 100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
