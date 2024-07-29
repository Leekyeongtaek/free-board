package com.mrlee.free_board.post.service;

import com.mrlee.free_board.common.CriticalException;
import com.mrlee.free_board.member.domain.Member;
import com.mrlee.free_board.member.repository.MemberRepository;
import com.mrlee.free_board.post.domain.*;
import com.mrlee.free_board.post.dto.request.*;
import com.mrlee.free_board.post.repository.*;
import com.mrlee.free_board.post.repository.query.dto.PostQueryDtoList;
import com.mrlee.free_board.post.repository.query.CommentQueryRepository;
import com.mrlee.free_board.post.repository.query.PostQueryRepository;
import com.mrlee.free_board.post.repository.query.dto.ChildCommentQueryDto;
import com.mrlee.free_board.post.repository.query.dto.CommentQueryDtoList;
import com.mrlee.free_board.post.repository.query.dto.PostQueryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostLikesMappingRepository postLikesMappingRepository;
    private final CommentRepository commentRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final CommentLikesMappingRepository commentLikesMappingRepository;
    private final MemberRepository memberRepository;

    public void addPost(PostSaveForm form) {
        if (form.getTitle().contains("critical")) {
            throw new CriticalException("CriticalException 발생.");
        }
        Member member = memberRepository.findById(form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원 정보를 찾을 수 없습니다."));
        Post post = form.toPostEntity(member);
        postRepository.save(post);
    }

    public void modifyPost(Long postId, PostUpdateForm form) {
        Post post = postRepository.findByIdAndMemberId(postId, form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
        post.update(form.getTitle(), form.getContent());
    }

    public PostQueryDto findPost(Long postId, Long loginMemberId) {
        return postQueryRepository.findPost(postId, loginMemberId);
    }

    public Page<PostQueryDtoList> searchPosts(Pageable pageable, PostSearchForm form) throws InterruptedException {
        if (form.getTitle() != null && form.getTitle().equals("stopTime")) {
            Thread.sleep(1500);
        }
        return postQueryRepository.searchPosts(pageable, form);
    }

    public void removePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
        post.hide();
    }

    public void requestPostLike(Long postId, PostLikesSaveForm form) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
        PostLikesId postLikesId = new PostLikesId(postId, form.getMemberId());
        Optional<PostLikesMapping> postLikesMapping = postLikesMappingRepository.findById(postLikesId);
        if (postLikesMapping.isPresent()) {
            postLikesMappingRepository.delete(postLikesMapping.get());
            post.minusLikes();
        } else {
            postLikesMappingRepository.save(new PostLikesMapping(postLikesId));
            post.plusLikes();
        }
    }

    public Page<CommentQueryDtoList> findParentComments(Long postId, Long loginMemberId, Pageable pageable) {
        return commentQueryRepository.findParentComments(postId, loginMemberId, pageable);
    }

    public Page<ChildCommentQueryDto> findChildComments(Long commentId, Pageable pageable, Long loginMemberId) {
        return commentQueryRepository.findChildComments(pageable, commentId, loginMemberId);
    }

    public void addParentComment(Long postId, CommentSaveForm form) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
        Member member = memberRepository.findById(form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원 정보를 찾을 수 없습니다."));
        commentRepository.save(new Comment(form.getContent(), post, member));
    }

    public void modifyComment(Long commentId, CommentUpdateForm form) {
        Comment comment = commentRepository.findByIdAndMemberId(commentId, form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 정보를 찾을 수 없습니다."));
        comment.update(form.getContent());
    }

    public void addChildComment(Long commentId, CommentSaveForm form) {
        Comment parentComment = commentRepository.findByIdAndPostJoinFetch(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 정보를 찾을 수 없습니다."));
        Member member = memberRepository.findById(form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원 정보를 찾을 수 없습니다."));
        Comment childComment = new Comment(form.getContent(), parentComment.getPost(), member);
        childComment.mappingParentComment(parentComment);
        commentRepository.save(childComment);
    }

    public void removeComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 정보를 찾을 수 없습니다."));
        comment.hide();
    }

    public void requestCommentLike(Long commentId, CommentLikesSaveForm form) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 정보를 찾을 수 없습니다."));

        CommentLikesId commentLikesId = new CommentLikesId(commentId, form.getMemberId());
        Optional<CommentLikesMapping> commentLikesMapping = commentLikesMappingRepository.findById(commentLikesId);

        if (commentLikesMapping.isPresent()) {
            commentLikesMappingRepository.delete(commentLikesMapping.get());
            comment.minusLikes();
        } else {
            commentLikesMappingRepository.save(new CommentLikesMapping(commentLikesId));
            comment.plusLikes();
        }
    }

//    public UrlResource testA(Long postId) throws MalformedURLException {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("해당하는 글을 찾을 수 없습니다."));
//        String absolutePath = "/Users/myhome/Desktop/project/free-board/src/main/resources/static/images/";
//        log.info("file download occurs postId={}, fileName={}", postId, fineName);
//        return new UrlResource("file:" + absolutePath + fineName);
//    }
}
