package com.mrlee.free_board.post.repository.query.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.mrlee.free_board.post.repository.query.dto.QChildCommentQueryDto is a Querydsl Projection type for ChildCommentQueryDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QChildCommentQueryDto extends ConstructorExpression<ChildCommentQueryDto> {

    private static final long serialVersionUID = -1550536724L;

    public QChildCommentQueryDto(com.querydsl.core.types.Expression<Long> commentId, com.querydsl.core.types.Expression<Long> parentCommentId, com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> author, com.querydsl.core.types.Expression<Integer> likes, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdDate, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Boolean> hiddenYn, com.querydsl.core.types.Expression<Boolean> likesYn, com.querydsl.core.types.Expression<Long> postAuthorMemberId) {
        super(ChildCommentQueryDto.class, new Class<?>[]{long.class, long.class, long.class, String.class, int.class, java.time.LocalDateTime.class, String.class, boolean.class, boolean.class, long.class}, commentId, parentCommentId, memberId, author, likes, createdDate, content, hiddenYn, likesYn, postAuthorMemberId);
    }

}

