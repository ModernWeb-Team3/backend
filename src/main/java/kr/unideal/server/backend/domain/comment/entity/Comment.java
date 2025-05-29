package kr.unideal.server.backend.domain.comment.entity;

import jakarta.persistence.*;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.global.entity.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean secret = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 생성자
    public static Comment of(User user, String content, Post post, Comment parent) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .parent(parent)
                .secret(false)
                .build();
    }

    // 도메인 메서드
    public void updateContent(String content, boolean secret) {
        this.content = content;
        this.secret = secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }
}
