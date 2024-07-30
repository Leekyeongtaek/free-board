package com.mrlee.free_board.post.repository.query.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.mrlee.free_board.post.repository.query.dto.QPostQueryDto is a Querydsl Projection type for PostQueryDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostQueryDto extends ConstructorExpression<PostQueryDto> {

    private static final long serialVersionUID = 388527081L;

    public QPostQueryDto(com.querydsl.core.types.Expression<? extends com.mrlee.free_board.post.domain.Post> post, com.querydsl.core.types.Expression<? extends com.mrlee.free_board.member.domain.Member> member, com.querydsl.core.types.Expression<Boolean> likeYn) {
        super(PostQueryDto.class, new Class<?>[]{com.mrlee.free_board.post.domain.Post.class, com.mrlee.free_board.member.domain.Member.class, boolean.class}, post, member, likeYn);
    }

}

