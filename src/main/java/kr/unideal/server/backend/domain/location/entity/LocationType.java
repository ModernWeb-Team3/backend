package kr.unideal.server.backend.domain.location.entity;

public enum LocationType {
    AI_IT("AI관·IT융합대학"),
    GACHON_HALL("가천관"),
    ENGINEERING1("공과대학1"),
    ENGINEERING2("공과대학2"),
    GLOBAL_CENTER("글로벌센터"),
    GRADUATE_EDU("교육대학원"),
    DORMITORY1("제1학생생활관"),
    DORMITORY2("제2학생생활관"),
    DORMITORY3("제3학생생활관"),
    GRADUATE_SCHOOL("대학원"),
    SEMICONDUCTOR("반도체대학"),
    BIO_NANO_COLLEGE("바이오나노대학"),
    BIO_NANO_RESEARCH("바이오나노연구원"),
    VISION_TOWER("비전타워"),
    INDUSTRY_COOP("산학협력관"),
    STUDENT_HALL("학생회관"),
    ARTS_SPORTS1("예술·체육대학1"),
    ARTS_SPORTS2("예술·체육대학2"),
    LIBRARY("중앙도서관"),
    KOREAN_MEDICINE("한의과대학");

    private final String label;

    LocationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}