package com.mrlee.free_board.post.domain;

import com.mrlee.free_board.common.AuditingTime;
import com.mrlee.free_board.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@Entity
public class Comment extends AuditingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private int likes;
    private boolean hiddenYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

//    @OneToMany(mappedBy = "parentComment")
//    private List<Comment> childCommentList = new ArrayList<>();

//    @OneToMany(mappedBy = "comment")
//    private List<SubComment> subComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(String content, Post post, Member member) {
        this.content = content;
        this.likes = 0;
        this.post = post;
        this.member = member;
        this.hiddenYn = false;
    }

    public void update(String content) {
        this.content = content;
    }

    public void mappingParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void plusLikes() {
        this.likes++;
    }

    public void minusLikes() {
        this.likes--;
    }

    public void hide() {
        this.hiddenYn = true;
    }
}
