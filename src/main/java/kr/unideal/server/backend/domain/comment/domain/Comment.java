package kr.unideal.server.backend.domain.comment.domain;

import jakarta.persistence.*;
import kr.unideal.server.backend.domain.post.domain.Post;
import kr.unideal.server.backend.global.common.BaseTimeEntity;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 게시글(Post)에 달린 댓글인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 부모 댓글 (null이면 일반 댓글, 값이 있으면 대댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글 목록
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean secret; // 비공개 여부

}
