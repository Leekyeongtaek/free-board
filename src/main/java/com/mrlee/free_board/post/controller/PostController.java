package com.mrlee.free_board.post.controller;

import com.mrlee.free_board.post.dto.request.*;
import com.mrlee.free_board.post.repository.query.dto.ChildCommentQueryDto;
import com.mrlee.free_board.post.repository.query.dto.CommentQueryDtoList;
import com.mrlee.free_board.post.repository.query.dto.PostQueryDto;
import com.mrlee.free_board.post.repository.query.dto.PostQueryDtoList;
import com.mrlee.free_board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> postAdd(@RequestBody PostSaveForm form) {
        postService.addPost(form);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> postModify(@PathVariable(name = "postId") Long postId, @RequestBody PostUpdateForm form) {
        postService.modifyPost(postId, form);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostQueryDto> postDetails(@PathVariable(name = "postId") Long postId, @RequestParam(name = "loginMemberId") Long loginMemberId) {
        PostQueryDto postQueryDto = postService.findPost(postId, loginMemberId);
        return new ResponseEntity<>(postQueryDto, OK);
    }

    @GetMapping
    public ResponseEntity<Page<PostQueryDtoList>> postList(Pageable pageable, @ModelAttribute PostSearchForm form) throws InterruptedException {
        Page<PostQueryDtoList> posts = postService.searchPosts(pageable, form);
        return new ResponseEntity<>(posts, OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> postRemove(@PathVariable(name = "postId") Long postId) {
        postService.removePost(postId);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> requestCommentLike(@PathVariable(name = "postId") Long postId, @RequestBody PostLikesSaveForm form) {
        postService.requestPostLike(postId, form);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("{postId}/comments")
    public ResponseEntity<Void> addParentComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentSaveForm form) {
        postService.addParentComment(postId, form);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Void> commentModify(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentUpdateForm form) {
        postService.modifyComment(commentId, form);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> commentRemove(@PathVariable(name = "commentId") Long commentId, @RequestParam(name = "memberId") Long memberId) {
        postService.removeComment(commentId, memberId);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<Void> addChildComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentSaveForm form) {
        postService.addChildComment(commentId, form);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> requestCommentLike(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentLikesSaveForm form) {
        postService.requestCommentLike(commentId, form);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentQueryDtoList>> parentCommentList(@PathVariable(name = "postId") Long postId, @RequestParam(name = "loginMemberId") Long loginMemberId,
                                                                       Pageable pageable) {
        Page<CommentQueryDtoList> result = postService.findParentComments(postId, loginMemberId, pageable);
        return new ResponseEntity<>(result, OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Page<ChildCommentQueryDto>> childCommentList(@PathVariable(name = "commentId") Long commentId, @RequestParam(name = "loginMemberId") Long loginMemberId,
                                                                       Pageable pageable) {
        Page<ChildCommentQueryDto> childComments = postService.findChildComments(commentId, pageable, loginMemberId);
        return new ResponseEntity<>(childComments, OK);
    }

    //    @GetMapping("/attach-file/{postId}")
//    public ResponseEntity<Resource> downloadAttachFile(@PathVariable(name = "postId") Long postId) throws MalformedURLException {
//        UrlResource resource = postService.testA(postId);
//        String encodedUploadFileName = UriUtils.encode("attachFile.jpeg", StandardCharsets.UTF_8);
//        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-Disposition", contentDisposition);
//        return new ResponseEntity<>(resource, httpHeaders, OK);
//    }

}
