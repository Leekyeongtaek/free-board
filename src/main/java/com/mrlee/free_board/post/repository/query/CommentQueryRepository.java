package com.mrlee.free_board.post.repository.query;

import com.mrlee.free_board.post.domain.QComment;
import com.mrlee.free_board.post.repository.query.dto.ChildCommentQueryDto;
import com.mrlee.free_board.post.repository.query.dto.CommentQueryDtoList;
import com.mrlee.free_board.post.repository.query.dto.QChildCommentQueryDto;
import com.mrlee.free_board.post.repository.query.dto.QCommentQueryDtoList;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mrlee.free_board.member.domain.QMember.member;
import static com.mrlee.free_board.post.domain.QComment.comment;
import static com.mrlee.free_board.post.domain.QCommentLikesMapping.commentLikesMapping;
import static com.mrlee.free_board.post.domain.QPost.post;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

//    public CommentQueryRepository(JPAQueryFactory queryFactory) {
//        this.queryFactory = queryFactory;
//    }

    /*
    쿼리 기록
    자식 댓글 개수 서브 쿼리 포함시 0, 1000 조회시 18848ms
    자식 댓글 개수 서브 쿼리 비포함시 0, 1000 조회시 130ms
    //mysql 쿼리 실행 결과 8배 차이
    //1. SubQuery : LIMIT 0, 2000 4.005ms
    //2. Group By : LIMIT 0, 2000 0.520ms -> 우선 2번 방식 선택
    //3. OneToMany 관계 매핑, childCommentList 변수 선언 후 : [LIMIT 0, 20 :434ms] [LIMIT 0, 500 : 7123ms]
    //4. sub_comment 테이블, SubComment 매핑 : 오래 걸림
     */
    public Page<CommentQueryDtoList> findParentComments(Long postId, Long loginMemberId, Pageable pageable) {

        QComment childComment = new QComment("childComment");

        List<CommentQueryDtoList> commentQueryDtoList = queryFactory
                .select(new QCommentQueryDtoList(comment.id, comment.content, comment.likes,
                        comment.createdDate, comment.hiddenYn, member.id, member.nickName,
                        commentLikesMapping.isNotNull(), childComment.count(), post.member.id))
                .from(comment)
                .join(comment.member, member)
                .join(comment.post, post)
                .leftJoin(commentLikesMapping).on(commentLikesMapping.id.commentId.eq(comment.id)
                        .and(commentLikesMapping.id.memberId.eq(loginMemberId)))
                .leftJoin(childComment).on(childComment.parentComment.id.eq(comment.id))
                .where(
                        comment.post.id.eq(postId),
                        comment.parentComment.id.isNull())
                .groupBy(comment.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(comment.count())
                .from(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.parentComment.id.isNull())
                .fetchOne();

        return new PageImpl<>(commentQueryDtoList, pageable, totalCount);
    }

    public Page<ChildCommentQueryDto> findChildComments(Pageable pageable, Long commentId, Long loginMemberId) {

        List<ChildCommentQueryDto> childCommentQueryDtoList = queryFactory
                .select(new QChildCommentQueryDto(
                        comment.id, comment.parentComment.id, member.id,
                        member.nickName, comment.likes, comment.createdDate, comment.content,
                        comment.hiddenYn, commentLikesMapping.isNotNull(), post.member.id))
                .from(comment)
                .join(comment.member, member)
                .join(comment.post, post)
                .leftJoin(commentLikesMapping).on(commentLikesMapping.id.commentId.eq(comment.id)
                        .and(commentLikesMapping.id.memberId.eq(loginMemberId)))
                .where(
                        comment.parentComment.id.eq(commentId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(comment.count())
                .from(comment)
                .where(
                        comment.parentComment.id.eq(commentId))
                .fetchOne();

        return new PageImpl<>(childCommentQueryDtoList, pageable, totalCount);
    }

}
