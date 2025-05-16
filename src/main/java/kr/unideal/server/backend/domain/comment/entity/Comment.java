package kr.unideal.server.backend.domain.comment.entity;

import jakarta.persistence.*;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.global.common.BaseTimeEntity;
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

    // 어떤 게시글(Post)에 달린 댓글인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 부모 댓글 (null이면 일반 댓글, 값이 있으면 대댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글 목록
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
    public void updateContent(String content) {
        this.content = content;
    }


}
