package com.mrlee.free_board.member.controller;

import com.mrlee.free_board.member.dto.request.MemberSaveForm;
import com.mrlee.free_board.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> memberAdd(@RequestBody MemberSaveForm form) {
        memberService.addMember(form);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
