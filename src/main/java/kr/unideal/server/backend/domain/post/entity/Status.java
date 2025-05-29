package kr.unideal.server.backend.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;


@Getter
public enum Status {
    ON_SALE("판매중"),
    SOLD_OUT("판매완료");

    private final String description;

    Status(String description) {
        this.description = description;

    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static Status fromDescription(String desc) {
        String cleaned = desc.trim().replaceAll("^\"|\"$", "");
        return Arrays.stream(Status.values())
                .filter(c -> c.description.equals(cleaned))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상태입니다: " + desc));
    }
}
