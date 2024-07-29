package com.mrlee.free_board.post.dto.request;

import com.mrlee.free_board.member.domain.Member;
import com.mrlee.free_board.post.domain.Post;
import com.mrlee.free_board.post.domain.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostSaveForm {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private Long memberId;
    private PostType type;

    public Post toPostEntity(Member member) {
        return new Post(title, content, type, member);
    }
}
