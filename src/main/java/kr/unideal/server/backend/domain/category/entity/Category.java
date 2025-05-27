package kr.unideal.server.backend.domain.category.entity;

  import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum Category {

    BOOK("책"),
    CLOTHING("의류"),
    STATIONERY("전공물품"),
    ELECTRONICS("전자기기"),
    FURNITURE("가구"),
    OTHER("기타");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 한글 설명으로 Category 찾기
     * JSON 직렬화 시 클라이언트로 "책", "의류" 등 description을 주고,
     * 다시 받았을 때 이 메서드가 매핑해 줍니다.
     */
    @JsonCreator
    public static Category fromDescription(String desc) {
        String cleaned = desc.trim().replaceAll("^\"|\"$", "");
        return Arrays.stream(Category.values())
                .filter(c -> c.description.equals(cleaned))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다: " + desc));
    }
}
