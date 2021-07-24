package Stockit.member.service;

import Stockit.member.domain.Member;
import Stockit.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest //스프링부트 띄우고 테스트(@Autowired 사용 위해)
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    private Member member;
    private Member member2;

    @BeforeEach
    public void 기본_설정() {
        member = new Member("회원1", "주린이1", "stockit@stockit.com", "abcdefg");
        member2 = new Member("회원2", "주린이2", "stockit2@stockit.com", "abcdefg2");
        memberService.join(member);
        memberService.join(member2);
    }

    @Test
    public void 회원가입() {
        //given

        //when
        Long saveId = member.getId();

        //then
        Assertions.assertThat(member).isEqualTo(memberRepository.findOne(saveId));
    }

    @Test
    public void 중복회원_예외() {
        //given
        Member duplicatedMember = new Member("회원1", "주린이1", "stockit@stockit.com", "abcdefg");

        //when

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(duplicatedMember));
    }

    @Test
    public void 모든_멤버_조회() {
        //given

        //when
        List<Member> allMembers = memberService.findAllMembers();

        //then
        Assertions.assertThat(allMembers.size()).isEqualTo(2);
    }

    @Test
    public void 특정_멤버_조회() {
        //given

        //when
        Member findMember = memberService.findOne(member.getId());

        //then
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}