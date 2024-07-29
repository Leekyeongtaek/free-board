package com.mrlee.free_board.post.domain;

import com.mrlee.free_board.common.AuditingTime;
import com.mrlee.free_board.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@Entity
public class Post extends AuditingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostType type;
    private int views;
    private int likes;
    private boolean hiddenYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private final List<Comment> commentList = new ArrayList<>();

    public Post(String title, String content, PostType type, Member member) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.member = member;
        this.views = 0;
        this.likes = 0;
        this.hiddenYn = false;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
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
