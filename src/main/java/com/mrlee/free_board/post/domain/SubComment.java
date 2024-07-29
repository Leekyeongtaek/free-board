package com.mrlee.free_board.post.domain;

import com.mrlee.free_board.common.AuditingTime;
import com.mrlee.free_board.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_comment")
@Entity
public class SubComment extends AuditingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String content;
    private int likes;
    private boolean hiddenYn;

    public SubComment(Comment comment, Member member, String content) {
        this.comment = comment;
        this.member = member;
        this.content = content;
        this.likes = 0;
        this.hiddenYn = false;
    }
}
