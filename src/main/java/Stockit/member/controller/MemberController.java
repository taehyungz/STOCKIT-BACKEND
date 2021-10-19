package Stockit.member.controller;

import Stockit.member.dto.*;
import Stockit.member.service.MemberService;
import Stockit.order.dto.OrderInfo;
import Stockit.response.ApiResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //모든 멤버 조회
    @GetMapping
    public ApiResponse<List<MemberInfo>> list() {
        return ApiResponse.ok(memberService.findAllMembers());
    }

    //멤버 생성
    @PostMapping
    public ApiResponse<Long> create(@RequestBody MemberJoinRequest form) {
        form.encodePassword(passwordEncoder.encode(form.getPassword()));
        return ApiResponse.ok(memberService.join(form));
    }

    //닉네임 중복 검사
    @GetMapping(value = "/login/valid-nickname")
    public ApiResponse<String> checkDuplicatedNickname(@RequestParam String nickname) {
        boolean duplicated = memberService.findDuplicatedNickname(nickname);
        if (duplicated) throw new IllegalArgumentException("닉네임 중복");
        return ApiResponse.ok(nickname);
    }

    //이메일 중복 검사
    @GetMapping(value = "/login/valid-email")
    public ApiResponse<String> checkDuplicatedEmail(@RequestParam String email) {
        boolean duplicated = memberService.findDuplicatedEmail(email);
        if (duplicated) throw new IllegalArgumentException("이메일 중복");
        return ApiResponse.ok(email);
    }

    //랭킹 조회
    @GetMapping(value = "/rank")
    public ApiResponse<List<RankingInfo>> getRankList() {
        return ApiResponse.ok(memberService.getRankList());
    }


    //로그인
    @PostMapping(value = "/login")
    public ApiResponse<MemberInfo> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.ok(memberService.login(loginRequest));
    }

    //주문 조회
    @GetMapping(value = "/{memberId}/orders")
    public ApiResponse<List<OrderInfo>> getOrders(@PathVariable Long memberId) throws NotFoundException {
        final MemberInfo memberInfo = memberService.findMember(memberId);
        return ApiResponse.ok(memberInfo.getOrders());
    }

    //보유 주식 조회
    @GetMapping(value = "/{memberId}/stocks")
    public ApiResponse<List<AccountStockInfo>> getMyStocks(@PathVariable Long memberId) throws NotFoundException {
        final MemberInfo memberInfo = memberService.findMember(memberId);
        return ApiResponse.ok(memberInfo.getStocks());
    }
}
