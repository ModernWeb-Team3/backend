

-- 1) User 더미 데이터 삽입
INSERT INTO `user` (id, email, password, name, is_verified) VALUES
                                                                (2, 'alice@gachon.ac.kr',    '$2a$10$aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'Alice',  TRUE),
                                                                (3, 'bob@gachon.ac.kr',      '$2a$10$bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'Bob',    TRUE),
                                                                (4, 'charlie@gachon.ac.kr',  '$2a$10$cccccccccccccccccccccccccccccccccccccccccccccc', 'Charlie', FALSE);

-- 2) Post 더미 데이터 삽입
--    BaseTimeEntity 상속으로 created_at, updated_at 컬럼이 있다면 기본값을 CURRENT_TIMESTAMP로 설정했다고 가정
INSERT INTO post (id, name, detail, price, status, category, user_id) VALUES
                                                                             (1, '책상 팝니다',   '사용감 거의 없는 스터디 책상입니다.',               50000, '', 1, 1),
                                                                             (2, '의자 팝니다',   '편안한 사무용 의자, 높낮이 조절 가능',            30000, '노출', 1, 1),
                                                                             (3, '노트북 팝니다', '게이밍용 노트북 i7, RAM 16GB, SSD 512GB',       600000, '숨김', 2, 2),
                                                                             (4, '모니터 팝니다', '27인치 QHD 모니터, 상태 A급',                    150000, '노출', 2, 3),
                                                                             (5, '책 팝니다',     '프로그래밍 입문서 세트 (총 5권)',               20000, '노출', 3, 1);
