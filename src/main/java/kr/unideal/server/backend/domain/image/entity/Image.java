package kr.unideal.server.backend.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.unideal.server.backend.domain.post.entity.Post;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url; // 이미지 저장된 경로 또는 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 이미지가 속한 게시글
}
