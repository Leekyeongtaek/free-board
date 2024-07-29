package com.mrlee.free_board.post.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDetails {

    private Long commentId;
    private String content;
    private String author;
    private int likes;
    private LocalDateTime createdDate;
    private List<CommentDetails> childCommentDetailsList;
}
