INSERT INTO `user` (id, email, password, name, is_verified)
VALUES (1, 'alice@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '박정현', TRUE),
       (2, 'bob@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '이수정', TRUE),
       (3, 'charlie@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '최민수', TRUE),
       (4, 'david@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '김서윤', TRUE),
       (5, 'emma@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '정우진', TRUE),
       (6, 'frank@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '한지민', TRUE),
       (7, 'grace@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '윤도현', TRUE),
       (8, 'helen@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '장하늘', TRUE),
       (9, 'ian@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '노지후', TRUE),
       (10, 'jane@gachon.ac.kr', '$2a$10$Dow1K8Pb3tHIMYyKvPbZTuIVKP2iFnV9a1XgAJcXx7AD5f6HyN6Xm', '백예린', TRUE);


-- Post 테이블 더미 데이터
INSERT INTO post (id, name, detail, price, status, created_at, user_id, category, location)
VALUES (1, '맥북 에어 M2 팝니다', '거의 새 제품입니다. 박스, 충전기 포함.', 1200000, 'ON_SALE', NOW(), 1, 'ELECTRONICS', 'IT_LIBRARY'),
       (2, '자전거 판매합니다', '알톤 MTB 26인치. 사용감 있으나 잘 굴러갑니다.', 150000, 'ON_SALE', NOW(), 2, 'OTHER', 'STUDENT_HALL'),
       (3, '아이패드 프로 3세대 11인치', '화면에 미세한 스크래치 있음. 애플펜슬 포함.', 850000, 'SOLD_OUT', NOW(), 3, 'ELECTRONICS', 'AI_BUILDING'),
       (4, '책상 + 의자 세트', '1년 사용. 자취방 정리로 급처분.', 70000, 'ON_SALE', NOW(), 4, 'FURNITURE', 'REAL_ESTATE'),
       (5, 'PS5 디스크 에디션', '박스 미개봉 새상품입니다.', 680000, 'SOLD_OUT', NOW(), 5, 'ELECTRONICS', 'VISION_TOWER'),
       (6, '에어팟 프로 2세대', '정품. 배터리 상태 매우 양호.', 190000, 'ON_SALE', NOW(), 6, 'ELECTRONICS', 'ENGINEERING1'),
       (7, 'LG 모니터 27인치 FHD', '사무용으로만 사용. 상태 좋음.', 120000, 'ON_SALE', NOW(), 7, 'ELECTRONICS', 'SEMICONDUCTOR'),
       (8, '닌텐도 스위치 OLED', '화이트 색상, 풀박스.', 380000, 'SOLD_OUT', NOW(), 8, 'ELECTRONICS', 'CENTRAL_LIBRARY'),
       (9, '한컴 오피스 2022 정품', '키 미사용. 정품 인증 가능.', 70000, 'ON_SALE', NOW(), 9, 'STATIONERY', 'GACHON'),
       (10, '카카오프렌즈 인형 세트', '라이언, 어피치 포함 총 6종.', 45000, 'SOLD_OUT', NOW(), 10, 'OTHER', 'ARTS_SPORTS');

-- 일반 댓글 6개
INSERT INTO comment (id, content, secret, post_id, parent_id, user_id, created_at)
VALUES (1, '좋은 상품이네요! 관심 있어요.', FALSE, 1, NULL, 2, NOW()),
       (2, '가격 조정 가능할까요?', TRUE, 1, NULL, 3, NOW()),
       (3, '혹시 실사용 기간이 얼마나 되나요?', FALSE, 3, NULL, 4, NOW()),
       (4, '구매하고 싶은데 직거래 가능할까요?', FALSE, 4, NULL, 5, NOW()),
       (5, '상태가 정말 괜찮아 보여요.', FALSE, 6, NULL, 6, NOW()),
       (6, '지역이 어디신가요?', TRUE, 6, NULL, 7, NOW());

-- 대댓글 4개
INSERT INTO comment (id, content, secret, post_id, parent_id, user_id, created_at)
VALUES (7, '네, 어느 정도까지 생각하시나요?', FALSE, 1, 2, 1, NOW()),
       (8, '서울 강남 근처입니다.', FALSE, 6, 6, 6, NOW()),
       (9, '직거래 가능합니다. 평일 저녁 가능해요.', FALSE, 4, 4, 5, NOW()),
       (10, '사용한 지는 약 6개월 됐어요.', FALSE, 3, 3, 4, NOW());


-- Image 테이블 더미 데이터 (각 상품당 2-3개의 이미지)
INSERT INTO image (url, post_id)
VALUES
-- 맥북 에어 M2 (post_id: 1)
('https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=800&h=600&fit=crop', 1),
('https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&h=600&fit=crop', 1),
('https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=800&h=600&fit=crop', 1),

-- 자전거 (post_id: 2)
('https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=800&h=600&fit=crop', 2),
('https://images.unsplash.com/photo-1571068316344-75bc76f77890?w=800&h=600&fit=crop', 2),

-- 아이패드 프로 (post_id: 3)
('https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800&h=600&fit=crop', 3),
('https://images.unsplash.com/photo-1561154464-82e9adf32764?w=800&h=600&fit=crop', 3),
('https://images.unsplash.com/photo-1585776245991-cf89dd7fc73a?w=800&h=600&fit=crop', 3),

-- 책상 + 의자 세트 (post_id: 4)
('https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&h=600&fit=crop', 4),
('https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800&h=600&fit=crop', 4),

-- PS5 (post_id: 5)
('https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=800&h=600&fit=crop', 5),
('https://images.unsplash.com/photo-1607853202273-797f1c22a38e?w=800&h=600&fit=crop', 5),

-- 에어팟 프로 (post_id: 6)
('https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=800&h=600&fit=crop', 6),
('https://images.unsplash.com/photo-1574920162544-39d0d25c5620?w=800&h=600&fit=crop', 6),

-- LG 모니터 (post_id: 7)
('https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&h=600&fit=crop', 7),
('https://images.unsplash.com/photo-1593640408182-31c70c8268f5?w=800&h=600&fit=crop', 7),

-- 닌텐도 스위치 (post_id: 8)
('https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800&h=600&fit=crop', 8),
('https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=800&h=600&fit=crop', 8),

-- 한컴 오피스 (post_id: 9)
('https://images.unsplash.com/photo-1551650975-87deedd944c3?w=800&h=600&fit=crop', 9),
('https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?w=800&h=600&fit=crop', 9),

-- 카카오프렌즈 인형 (post_id: 10)
('https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=800&h=600&fit=crop', 10),
('https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=800&h=600&fit=crop', 10);
