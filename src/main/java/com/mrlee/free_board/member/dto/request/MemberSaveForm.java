package com.mrlee.free_board.member.dto.request;

import com.mrlee.free_board.utils.MyHashUtils;
import com.mrlee.free_board.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSaveForm {

    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickName;

    public Member toMemberEntity() {
        return Member.builder()
                .userId(userId)
                .password(MyHashUtils.convertToSHA256(password))
                .nickName(nickName)
                .build();
    }
}
