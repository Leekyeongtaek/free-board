package com.mrlee.free_board.post.repository.query;

import com.mrlee.free_board.post.domain.PostType;
import com.mrlee.free_board.post.dto.request.PostSearchForm;
import com.mrlee.free_board.post.repository.query.dto.PostQueryDto;
import com.mrlee.free_board.post.repository.query.dto.PostQueryDtoList;
import com.mrlee.free_board.post.repository.query.dto.QPostQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.mrlee.free_board.member.domain.QMember.member;
import static com.mrlee.free_board.post.domain.QPost.post;
import static com.mrlee.free_board.post.domain.QPostLikesMapping.postLikesMapping;

@Slf4j
@Repository
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PostQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public PostQueryDto findPost(Long postId, Long loginMemberId) {

        PostQueryDto postQueryDto = queryFactory.select(new QPostQueryDto(post, member, postLikesMapping))
                .from(post)
                .join(post.member, member)
                .leftJoin(postLikesMapping).on(post.id.eq(postLikesMapping.id.postId)
                        .and(postLikesMapping.id.memberId.eq(loginMemberId)))
                .where(post.id.eq(postId))
                .fetchOne();

        if (postQueryDto == null) {
            throw new IllegalArgumentException("해당하는 글을 찾을 수 없습니다.");
        }

        queryFactory.update(post)
                .set(post.views, post.views.add(1))
                .where(post.id.eq(postId))
                .execute();

        return postQueryDto;
    }

    //20 십만 건 : 2.2배 차이
    //쿼리프로젝션 엔티티 시간 : 1985ms
    //쿼리프로젝션 필드 시간 : 867ms -> 서브 쿼리 추가 후 1893ms
    public Page<PostQueryDtoList> searchPosts(Pageable pageable, PostSearchForm form) {

        List<PostQueryDtoList> postQueryDtoLists = queryFactory
                .select(Projections.constructor(PostQueryDtoList.class,
                        post.id, post.title, post.content,
                        post.views, post.likes, post.type,
                        member.nickName, post.createdDate, post.commentList.size()))
                .from(post)
                .join(post.member, member)
                .where(
                        postTitleContains(form.getTitle()),
                        postTypeEq(form.getType()),
                        post.hiddenYn.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        postTitleContains(form.getTitle()),
                        postTypeEq(form.getType()),
                        post.hiddenYn.eq(false))
                .fetchOne();

        return new PageImpl<>(postQueryDtoLists, pageable, totalCount);
    }

    private BooleanExpression postTitleContains(String title) {
        return StringUtils.hasText(title) ? post.title.contains(title) : null;
    }

    private BooleanExpression postTypeEq(PostType type) {
        return type != null ? post.type.eq(type) : null;
    }
}
