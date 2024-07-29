package com.mrlee.free_board.post.repository.query.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.mrlee.free_board.post.repository.query.dto.QCommentQueryDtoList is a Querydsl Projection type for CommentQueryDtoList
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentQueryDtoList extends ConstructorExpression<CommentQueryDtoList> {

    private static final long serialVersionUID = -72382014L;

    public QCommentQueryDtoList(com.querydsl.core.types.Expression<Long> commentId, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> likes, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdDate, com.querydsl.core.types.Expression<Boolean> hiddenYn, com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> author, com.querydsl.core.types.Expression<Boolean> likesYn, com.querydsl.core.types.Expression<Long> childCommentCount, com.querydsl.core.types.Expression<Long> postAuthorMemberId) {
        super(CommentQueryDtoList.class, new Class<?>[]{long.class, String.class, int.class, java.time.LocalDateTime.class, boolean.class, long.class, String.class, boolean.class, long.class, long.class}, commentId, content, likes, createdDate, hiddenYn, memberId, author, likesYn, childCommentCount, postAuthorMemberId);
    }

}

