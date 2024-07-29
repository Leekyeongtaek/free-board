package com.mrlee.free_board.member.service;

import com.mrlee.free_board.member.domain.Member;
import com.mrlee.free_board.member.dto.request.MemberSaveForm;
import com.mrlee.free_board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void addMember(MemberSaveForm form){
        Member member = form.toMemberEntity();
        memberRepository.save(member);
    }
}
