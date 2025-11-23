-- Auto-generated from src/main/resources/db/migration/V2__seed_data.sql
INSERT INTO roles (code, description)
VALUES ('ADMIN', 'Administrator'),
       ('USER', 'End user')
ON DUPLICATE KEY UPDATE description = VALUES(description);

INSERT INTO users (username, full_name, email, password_hash, status, primary_role)
VALUES ('admin', 'Administrator', 'admin@wedding.local', '$2a$10$zVr31i8t0teuN0N7bYIZVuV6d3cYE3cLBYktnA4Ancc7P6BAIQFse', 'ACTIVE', 'ADMIN')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'ADMIN'
WHERE u.username = 'admin'
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

INSERT INTO halls (code, name, capacity, base_price, description, amenities, image_url, display_order)
VALUES ('GRAND', 'Grand Ballroom', 500, 150000000, 'Sảnh lớn sang trọng phù hợp tiệc cưới cao cấp',
        JSON_ARRAY('LED Wall', 'Premium Sound', 'Private Lounge'),
        'https://example.com/images/halls/grand.jpg', 1),
       ('GARDEN', 'Garden View', 300, 95000000, 'Sảnh ngoài trời phong cách nhiệt đới',
        JSON_ARRAY('Outdoor Stage', 'Live Music', 'Decor Lighting'),
        'https://example.com/images/halls/garden.jpg', 2)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO menus (name, price, description, category, image_url, is_featured)
VALUES ('Luxury Oriental', 3500000, 'Thực đơn Á cao cấp 10 món', 'ORIENTAL',
        'https://example.com/images/menus/oriental.jpg', 1),
       ('Western Delight', 3200000, 'Thực đơn Âu hiện đại', 'WESTERN',
        'https://example.com/images/menus/western.jpg', 0)
ON DUPLICATE KEY UPDATE price = VALUES(price);

INSERT INTO promotions (title, slug, description, start_date, end_date, terms)
VALUES ('Early Bird 2025', 'early-bird-2025', 'Ưu đãi giảm 15% cho hợp đồng ký trước 31/03/2025',
        '2025-01-01', '2025-03-31', 'Áp dụng cho hợp đồng có giá trị từ 200 triệu'),
       ('Summer Love', 'summer-love', 'Tặng gói trang trí cao cấp cho tiệc cưới tháng 6-8',
        '2025-06-01', '2025-08-31', 'Không áp dụng đồng thời chương trình khác')
ON DUPLICATE KEY UPDATE description = VALUES(description);
