-- 카테고리 데이터
INSERT INTO video_categories (name, description, icon, display_order, is_active, created_at, updated_at) VALUES
                                                                                                             ('보이스피싱', '보이스피싱 예방 교육 영상', 'phone_warning', 1, true, NOW(), NOW()),
                                                                                                             ('스미싱', '스미싱 예방 교육 영상', 'message_warning', 2, true, NOW(), NOW()),
                                                                                                             ('대출사기', '대출 사기 예방 교육 영상', 'money_warning', 3, true, NOW(), NOW()),
                                                                                                             ('파밍', '파밍 예방 교육 영상', 'security_warning', 4, true, NOW(), NOW());

-- 보이스피싱 영상 (금융감독원 실제 영상들)
-- 보이스피싱 영상 (카테고리 ID = 1)
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('보이스피싱, 예방이 중요합니다!', '보이스피싱 예방법과 실제 사례를 통해 예방 의식을 높이는 영상',
     'https://www.youtube.com/watch?v=4Te3IV1PE0Q', '4Te3IV1PE0Q',
     'https://img.youtube.com/vi/4Te3IV1PE0Q/hqdefault.jpg', '4:20', 0, 1, true, 1, NOW(), NOW()),

    ('금융사기(보이스피싱) 대처법 │ 행정안전부', '보이스피싱 피해 예방과 대처법을 소개하는 행정안전부 공식 영상',
     'https://www.youtube.com/watch?v=jFdg0_b-BHc', 'jFdg0_b-BHc',
     'https://img.youtube.com/vi/jFdg0_b-BHc/hqdefault.jpg', '5:02', 0, 2, true, 1, NOW(), NOW()),

    ('[#유퀴즈] 어이~보이스피싱범 보고 있나?', '보이스피싱 예방부터 해결 방법까지 유쾌하게 알려주는 유퀴즈 영상',
     'https://www.youtube.com/watch?v=FbLav4Rmfdg', 'FbLav4Rmfdg',
     'https://img.youtube.com/vi/FbLav4Rmfdg/hqdefault.jpg', '6:12', 0, 3, true, 1, NOW(), NOW()),

    ('"와... 진짜 은행 직원인 줄" 보이스피싱 실제 대화', '실제 보이스피싱 전화를 재현한 사례 영상',
     'https://www.youtube.com/watch?v=PDCKon2qIes', 'PDCKon2qIes',
     'https://img.youtube.com/vi/PDCKon2qIes/hqdefault.jpg', '3:54', 0, 4, true, 1, NOW(), NOW()),

    ('보이스피싱 피해 예방 6가지 레시피', '보이스피싱 피해를 막기 위한 6가지 핵심 예방법을 제시하는 영상',
     'https://www.youtube.com/watch?v=c4ZVbT___iw', 'c4ZVbT___iw',
     'https://img.youtube.com/vi/c4ZVbT___iw/hqdefault.jpg', '4:41', 0, 5, true, 1, NOW(), NOW()),

    ('보이스피싱 피해 예방 캠페인', '보이스피싱 범죄의 위험성을 알리고 예방을 독려하는 공익 캠페인 영상',
     'https://www.youtube.com/watch?v=YQLtTFO9xn4', 'YQLtTFO9xn4',
     'https://img.youtube.com/vi/YQLtTFO9xn4/hqdefault.jpg', '3:58', 0, 6, true, 1, NOW(), NOW()),

    ('보이스피싱예방전 늘 꼭 또', '보이스피싱 예방을 위한 유머러스한 홍보 영상',
     'https://www.youtube.com/watch?v=xNCMXdrqlu4', 'xNCMXdrqlu4',
     'https://img.youtube.com/vi/xNCMXdrqlu4/hqdefault.jpg', '4:10', 0, 7, true, 1, NOW(), NOW()),

    ('[NH농협은행] 보이스피싱 사칭 사기 피해 예방 편', '은행 사칭 보이스피싱의 유형과 예방 방법을 알려주는 NH농협은행 영상',
     'https://www.youtube.com/watch?v=EGeORAAYjYk', 'EGeORAAYjYk',
     'https://img.youtube.com/vi/EGeORAAYjYk/hqdefault.jpg', '5:05', 0, 8, true, 1, NOW(), NOW());
-- 스미싱 영상
-- 스미싱 예방 영상 (카테고리 ID = 2)
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('[공익광고협의회] 2019 피싱 스미싱 피해 예방 캠페인 - 누구나 (온라인)', '스미싱 피해를 예방하기 위한 공익광고 캠페인 영상',
     'https://www.youtube.com/watch?v=8p6EbgGI4UQ', '8p6EbgGI4UQ',
     'https://img.youtube.com/vi/8p6EbgGI4UQ/hqdefault.jpg', '4:05', 0, 1, true, 2, NOW(), NOW()),

    ('스미싱 예방하기│백종우의 스마트한 금융 생활 완전 정복', '스미싱 예방 요령과 안전한 금융 생활 방법을 안내하는 영상',
     'https://www.youtube.com/watch?v=psa_93hNWCQ', 'psa_93hNWCQ',
     'https://img.youtube.com/vi/psa_93hNWCQ/hqdefault.jpg', '5:12', 0, 2, true, 2, NOW(), NOW()),

    ('보이스피싱・스미싱 이것만 알고 예방하자!', '보이스피싱과 스미싱의 차이점 및 예방법을 소개하는 영상',
     'https://www.youtube.com/watch?v=Dc5UDTHU3Mo', 'Dc5UDTHU3Mo',
     'https://img.youtube.com/vi/Dc5UDTHU3Mo/hqdefault.jpg', '4:33', 0, 3, true, 2, NOW(), NOW()),

    ('중국 스미싱 사기꾼에게 직접 들은 그들의 사기수법', '스미싱 사기 조직의 실제 수법을 소개하는 탐사보도 영상',
     'https://www.youtube.com/watch?v=7El2tW2-lq8', '7El2tW2-lq8',
     'https://img.youtube.com/vi/7El2tW2-lq8/hqdefault.jpg', '6:24', 0, 4, true, 2, NOW(), NOW()),

    ('택배 문자 눌렀는데 4억 원 증발, 너무 쉽게 털려버린 이유는?', '택배 스미싱을 통한 금융 피해 사례와 예방책을 다룬 뉴스 영상',
     'https://www.youtube.com/watch?v=jUOaGS4iIvA', 'jUOaGS4iIvA',
     'https://img.youtube.com/vi/jUOaGS4iIvA/hqdefault.jpg', '5:01', 0, 5, true, 2, NOW(), NOW()),

    ('민생쿠폰 문자에 링크가? 스미싱 사기 주의하세요!', '정부 지원금 사칭 스미싱 문자 사례와 예방 수칙 안내',
     'https://www.youtube.com/watch?v=U1zv8I-quNU', 'U1zv8I-quNU',
     'https://img.youtube.com/vi/U1zv8I-quNU/hqdefault.jpg', '4:18', 0, 6, true, 2, NOW(), NOW()),

    ('스미싱 문자 클릭 시 사기 피해 막는법 (3편)', '스미싱 문자 클릭 후 대처법과 피해 방지 요령을 다룬 영상',
     'https://www.youtube.com/watch?v=_heZ9Ol4LS8', '_heZ9Ol4LS8',
     'https://img.youtube.com/vi/_heZ9Ol4LS8/hqdefault.jpg', '4:42', 0, 7, true, 2, NOW(), NOW()),

    ('제대로 알고 예방하자! 보이스 피싱·스미싱', '보이스피싱과 스미싱을 함께 다루며 올바른 예방 방법을 설명하는 영상',
     'https://www.youtube.com/watch?v=A2HEUAtFSpo', 'A2HEUAtFSpo',
     'https://img.youtube.com/vi/A2HEUAtFSpo/hqdefault.jpg', '3:59', 0, 8, true, 2, NOW(), NOW());

-- 대출사기 영상
-- 대출사기 영상 (category_id = 3)
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('내 돈을 노린다! 대환 대출 사기 수법 공개 🚨', '대환 대출 사기, 문자와 전화로 유도하는 실제 수법 사례',
     'https://www.youtube.com/watch?v=Mn_qD6_6YtM', 'Mn_qD6_6YtM',
     'https://img.youtube.com/vi/Mn_qD6_6YtM/hqdefault.jpg', '4:37', 0, 1, true, 3, NOW(), NOW()),

    ('"더 싼 대출로 갈아타라"', '저금리로 갈아타라며 유도하는 대환 대출 사기 유형',
     'https://www.youtube.com/watch?v=jXJBWdyZr4Y', 'jXJBWdyZr4Y',
     'https://img.youtube.com/vi/jXJBWdyZr4Y/hqdefault.jpg', '3:55', 0, 2, true, 3, NOW(), NOW()),

    ('저금리 대출 보이스피싱 사기', '“대출금리 연 1%대”를 미끼로 한 저금리 대출 보이스피싱 사례',
     'https://www.youtube.com/watch?v=9TExyYoz1zY', '9TExyYoz1zY',
     'https://img.youtube.com/vi/9TExyYoz1zY/hqdefault.jpg', '19:02', 0, 3, true, 3, NOW(), NOW()),

    ('"대출 이자 내셔야죠", "돈 빌린 적 없는데요?"', '대출 이자 납부를 요구하는 신종 보이스피싱 경고 사례',
     'https://www.youtube.com/watch?v=80QhMsC8w98', '80QhMsC8w98',
     'https://img.youtube.com/vi/80QhMsC8w98/hqdefault.jpg', '3:44', 0, 4, true, 3, NOW(), NOW()),

    ('[친절한法] 비대면 금융과 대출 사기', '비대면 금융거래 시대, 주의해야 할 대출 사기 유형을 법적으로 안내',
     'https://www.youtube.com/watch?v=myMRIaTHKRg', 'myMRIaTHKRg',
     'https://img.youtube.com/vi/myMRIaTHKRg/hqdefault.jpg', '5:12', 0, 5, true, 3, NOW(), NOW()),

    ('대출 사기 주의보 ‥ 은행 약관·공문도 조작', '공문서·약관을 위조하는 신종 대출 사기 수법',
     'https://www.youtube.com/watch?v=cAzfLcW6IfE', 'cAzfLcW6IfE',
     'https://img.youtube.com/vi/cAzfLcW6IfE/hqdefault.jpg', '6:25', 0, 6, true, 3, NOW(), NOW()),

    ('"금리 낮춰 줄게"... 대출 환승 노린 보이스피싱 기승', '대출 환승을 빌미로 개인정보를 탈취하는 사례',
     'https://www.youtube.com/watch?v=YkWbLTWEy7s', 'YkWbLTWEy7s',
     'https://img.youtube.com/vi/YkWbLTWEy7s/hqdefault.jpg', '4:58', 0, 7, true, 3, NOW(), NOW()),

    ('"대출금 1억 7천, 이자 16%"… 급증하는 사기 수법', '서민 대상 불법 고금리 대출 사기 사례와 주의점',
     'https://www.youtube.com/watch?v=nMu6lrK76u0', 'nMu6lrK76u0',
     'https://img.youtube.com/vi/nMu6lrK76u0/hqdefault.jpg', '5:47', 0, 8, true, 3, NOW(), NOW());
-- 파밍 영상
INSERT INTO prevention_videos (title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at) VALUES
                                                                                                                                                                                    ('파밍 피해 예방', '위장 사이트 주의', 'https://www.youtube.com/watch?v=pAOkUBpKI1Y', 'pAOkUBpKI1Y', 'https://img.youtube.com/vi/pAOkUBpKI1Y/hqdefault.jpg', '4:30', 0, 1, true, 4, NOW(), NOW()),
                                                                                                                                                                                    ('파밍 사이트 구별법', 'URL 확인 방법', 'https://www.youtube.com/watch?v=M7lc1UVf-VE', 'M7lc1UVf-VE', 'https://img.youtube.com/vi/M7lc1UVf-VE/hqdefault.jpg', '3:50', 0, 2, true, 4, NOW(), NOW());