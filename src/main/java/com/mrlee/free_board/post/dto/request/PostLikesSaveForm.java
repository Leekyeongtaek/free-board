package com.mrlee.free_board.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostLikesSaveForm {

    @NotBlank
    private Long memberId;
}
