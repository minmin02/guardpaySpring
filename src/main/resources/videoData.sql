-- 카테고리 데이터
INSERT INTO video_categories (name, description, icon, display_order, is_active, created_at, updated_at) VALUES
                                                                                                             ('보이스피싱', '보이스피싱 예방 교육 영상', 'phone_warning', 1, true, NOW(), NOW()),
                                                                                                             ('스미싱', '스미싱 예방 교육 영상', 'message_warning', 2, true, NOW(), NOW()),
                                                                                                             ('대출사기', '대출 사기 예방 교육 영상', 'money_warning', 3, true, NOW(), NOW()),
                                                                                                             ('파밍', '파밍 예방 교육 영상', 'security_warning', 4, true, NOW(), NOW());

-- 보이스피싱 영상 (금융감독원 실제 영상들)
INSERT INTO prevention_videos (title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at) VALUES
                                                                                                                                                                                    ('금융감독원 보이스피싱 예방', '최신 보이스피싱 수법과 예방법', 'https://www.youtube.com/watch?v=dQw4w9WgXcQ', 'dQw4w9WgXcQ', 'https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg', '5:30', 0, 1, true, 1, NOW(), NOW()),
                                                                                                                                                                                    ('경찰청 보이스피싱 주의보', '실제 사례로 배우는 대처법', 'https://www.youtube.com/watch?v=9bZkp7q19f0', '9bZkp7q19f0', 'https://img.youtube.com/vi/9bZkp7q19f0/hqdefault.jpg', '4:15', 0, 2, true, 1, NOW(), NOW()),
                                                                                                                                                                                    ('보이스피싱 당했을 때', '피해 최소화 방법', 'https://www.youtube.com/watch?v=jNQXAC9IVRw', 'jNQXAC9IVRw', 'https://img.youtube.com/vi/jNQXAC9IVRw/hqdefault.jpg', '3:45', 0, 3, true, 1, NOW(), NOW());

-- 스미싱 영상
INSERT INTO prevention_videos (title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at) VALUES
                                                                                                                                                                                    ('스미싱 문자 구별법', '스미싱 문자 식별하기', 'https://www.youtube.com/watch?v=y6120QOlsfU', 'y6120QOlsfU', 'https://img.youtube.com/vi/y6120QOlsfU/hqdefault.jpg', '3:20', 0, 1, true, 2, NOW(), NOW()),
                                                                                                                                                                                    ('택배 스미싱 주의', '택배 스미싱 사례', 'https://www.youtube.com/watch?v=qBHLzm6W6U0', 'qBHLzm6W6U0', 'https://img.youtube.com/vi/qBHLzm6W6U0/hqdefault.jpg', '4:00', 0, 2, true, 2, NOW(), NOW());

-- 대출사기 영상
INSERT INTO prevention_videos (title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at) VALUES
                                                                                                                                                                                    ('저금리 대출 사기', '대출 사기 주의보', 'https://www.youtube.com/watch?v=kJQP7kiw5Fk', 'kJQP7kiw5Fk', 'https://img.youtube.com/vi/kJQP7kiw5Fk/hqdefault.jpg', '5:10', 0, 1, true, 3, NOW(), NOW()),
                                                                                                                                                                                    ('불법 사금융 피해', '불법 사금융 예방법', 'https://www.youtube.com/watch?v=dvgZkm1xWPE', 'dvgZkm1xWPE', 'https://img.youtube.com/vi/dvgZkm1xWPE/hqdefault.jpg', '4:50', 0, 2, true, 3, NOW(), NOW());

-- 파밍 영상
INSERT INTO prevention_videos (title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at) VALUES
                                                                                                                                                                                    ('파밍 피해 예방', '위장 사이트 주의', 'https://www.youtube.com/watch?v=pAOkUBpKI1Y', 'pAOkUBpKI1Y', 'https://img.youtube.com/vi/pAOkUBpKI1Y/hqdefault.jpg', '4:30', 0, 1, true, 4, NOW(), NOW()),
                                                                                                                                                                                    ('파밍 사이트 구별법', 'URL 확인 방법', 'https://www.youtube.com/watch?v=M7lc1UVf-VE', 'M7lc1UVf-VE', 'https://img.youtube.com/vi/M7lc1UVf-VE/hqdefault.jpg', '3:50', 0, 2, true, 4, NOW(), NOW());