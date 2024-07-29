package com.mrlee.free_board.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostUpdateForm {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private Long memberId;
}
