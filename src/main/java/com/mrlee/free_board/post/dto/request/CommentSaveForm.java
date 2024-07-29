package com.mrlee.free_board.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveForm {

    @NotBlank
    private Long memberId;

    @NotBlank
    private String content;
}
