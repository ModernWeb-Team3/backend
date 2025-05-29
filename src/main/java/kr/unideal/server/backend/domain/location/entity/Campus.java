package kr.unideal.server.backend.domain.location.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum Campus {
    GACHON("가천관"),
    ENGINEERING1("공과대학1"),
    ENGINEERING2("공과대학2"),
    MEDICAL("한의과대학"),
    BIO_RESEARCH("바이오나노연구원"),
    LAW("법과대학"),
    VISION_TOWER("비전타워"),
    IT_LIBRARY("전자정보도서관"),
    AI_BUILDING("AI공학관"),
    SEMICONDUCTOR("반도체대학"),
    GLOBAL_CENTER("글로벌센터"),
    ARTS_SPORTS("예술체육대학"),
    GRAD_SCHOOL("교육대학원"),
    CENTRAL_LIBRARY("중앙도서관"),
    STUDENT_HALL("학생회관"),
    REAL_ESTATE("바람개비동산"),
    GACHEON_STATION("가천대역");

    private final String description;

    Campus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static Campus fromDescription(String desc) {
        String cleaned = desc.trim().replaceAll("^\"|\"$", "");
        return Arrays.stream(Campus.values())
                .filter(c -> c.description.equals(cleaned))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 캠퍼스입니다: " + desc));
    }
}
